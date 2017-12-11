import breeze.linalg.{DenseMatrix => BDM, DenseVector => BDV}
import org.apache.spark.mllib.classification.{NaiveBayes, NaiveBayesModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.regression.LabeledPoint

val RECOMMEND_LIMIT = 10
val HASHTAGS = sc.textFile("tags.txt").toArray
val RETWEET_PERCENTAGES = sc.textFile("retweet_percentages.txt").toArray
val TRAINING_MATRIX = sc.textFile("mat.txt")
val VOCABULARY = sc.textFile("vocab.txt").toArray

val parsedData = TRAINING_MATRIX.filter(line => line.split(',').length >=2).map({line =>
  val parts = line.split(',')
  val indices = parts(1).split(' ').map(_.toInt)
  val values = Array.fill[Double](indices.length)(1)
  LabeledPoint(parts(0).toDouble, Vectors.sparse(VOCABULARY.length, indices.sorted, values))
})

val splits = parsedData.randomSplit(Array(0.7, 0.3), seed = 11L)
val training = splits(0)
val test = splits(1)

val model = NaiveBayes.train(training, lambda = 1.0)
val predictionAndLabel = test.map(p => (model.predict(p.features), p.label))
val accuracy = 1.0 * predictionAndLabel.filter(x => x._1 == x._2).count() / test.count()
println(accuracy)

#######
DEMO
#######

val DEMO_TWEET = "Zero-Day Vulnerabilities Matter, but Don?t Ignore Known Issues"

#######

val tokens = DEMO_TWEET.split(' ').flatMap({ t => ("[a-zA-Z]+".r.findAllIn(t)).map(_.toLowerCase)})
val demo_array = Array.fill[Double](VOCABULARY.length)(0)
VOCABULARY.view.zipWithIndex.filter(item => tokens.contains(item._1)).foreach(item => demo_array(item._2) = 1)

val bdv = new BDV(demo_array)
val brzPi = new BDV[Double](model.pi)
val brzTheta = new BDM[Double](model.theta.length, model.theta(0).length)
  {
    // Need to put an extra pair of braces to prevent Scala treating `i` as a member.
    var i = 0
    while (i < model.theta.length) {
      var j = 0
      while (j < model.theta(i).length) {
        brzTheta(i, j) = model. theta(i)(j)
        j += 1
      }
      i += 1
    }
  }
val ratings = brzPi + brzTheta * bdv

val top_hashtag_indices = ratings.toArray.view.zipWithIndex.toSeq.sortBy(_._1).reverse.slice(0,RECOMMEND_LIMIT).map(_._2)
val relevant_hashtags = HASHTAGS.view.zipWithIndex.filter(item => top_hashtag_indices.contains(item._2)).map(_._1)
relevant_hashtags.foreach(println)

val relevant_retweet_percentages = RETWEET_PERCENTAGES.view.zipWithIndex.filter(item => top_hashtag_indices.contains(item._2))
val top_hashtag_indices = relevant_retweet_percentages.toSeq.sortBy(_._1).reverse.map(_._2)
for (i <- top_hashtag_indices) println(HASHTAGS(i) + " " + RETWEET_PERCENTAGES(i))
