/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jurassic.sh;

import com.google.gson.Gson;
import com.twitter.Hashtag;
import com.twitter.Tweet;
import com.twitter.utils.Utils;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import scala.Tuple2;

public class PreProcessing {
  
  

  public static void main(String args[]) {
    SparkConf conf = new SparkConf().setAppName("Tweet Pre-rocessing");
    JavaSparkContext sc = new JavaSparkContext(conf);
    File[] files = new File("/home/gualiu/data2").listFiles();
    
    List<String> tagsList = new ArrayList<String>();
    for(File f : files) {
      tagsList.add(f.getName().replaceAll(".json", ""));
    }
    
    String outDir = "/home/gualiu/out/";
    JavaRDD<String> dataSet = sc.textFile("/home/gualiu/data2");
    System.out.println("total lines of tweets: " + dataSet.count());
    String stopwordFile = "twitter/stopword.txt";
    String slangDictFile = "twitter/slangDict.txt";
    String dict = "twitter/wordsEn.txt";
    
    //read stop words and put them into an List<String> RDD
    final HashSet<String> stopWordSet = new HashSet<>(sc.textFile(stopwordFile).collect());
    
    final Map<String, String> slangDict = 
      sc.textFile(slangDictFile).mapToPair(new PairFunction<String, String, String>() {

        @Override
        public Tuple2<String, String> call(String line) throws Exception {
          String[] words = line.split("\\s+-\\s");
          assert words.length == 2;
          return new Tuple2(words[0], words[1]);
        }
      }).collectAsMap();//JavaPairRDD<String, String>
    
  JavaRDD<Tweet> tweetsRDD = dataSet
    .filter(new Function<String, Boolean>() {
      //get rid of invalid tweets
      @Override
      public Boolean call(String line) throws Exception {
        Gson gson = new Gson();
        Tweet t = null;
        try {
          t = gson.fromJson(line, Tweet.class);
        }
        catch (Exception e) {
          System.out.println(e.getMessage() + " : " + line);
          return false;
        }
        return t != null && t.getEntities() != null;
      }
    }).map(new Function<String, Tweet>() {
      //convert string to Tweet object
      @Override
      public Tweet call(String line) {
        Gson gson = new Gson();
        Tweet t = new Tweet();
        t = gson.fromJson(line, Tweet.class);
        return t;
      }
    }).filter(new Function<Tweet, Boolean>() {
      //get rid of non english tweets;
      @Override
      public Boolean call(Tweet t) throws Exception {
        return t.getLang().equals("en");
      }
    }).map(new Function<Tweet, Tweet>() {
      // remove URL, html entities, hash tags, digits, punctuations
      @Override
      public Tweet call(Tweet t) throws Exception {
        String cleanText = t.getText().replaceAll("(https?:\\/\\/)?([\\da-z\\.-]+)\\.([a-z\\.]{2,6})([\\/\\w\\.-]*)*\\/?", "")
                .replaceAll("<[^>]*>[\\w ]+<\\/[^>]*>"," ")
                .replaceAll("#"," ")
                .replaceAll("[0-9]+", " ")
                .replaceAll("[!\"#$%&()*+,-.\\/:;<=>?@\\[\\\\\\]^_`{|}~]", " ");
        t.setText(cleanText);
        return t;
      }
    }).filter(new Function<Tweet, Boolean>() {
      // get rid of tweets whose text is no more than one word;
      @Override
      public Boolean call(Tweet t) throws Exception {
        String[] words = t.getText().split("\\s+");
        return words.length > 1;
      }
    }).map(new Function<Tweet, Tweet>() {
      // convert slang and remove stop words
      @Override
      public Tweet call(Tweet t) throws Exception {
        StringBuilder noSlangText = new StringBuilder();
        StringBuilder clearText = new StringBuilder();
        String[] words = t.getText().split("\\s+");
        for (String word : words) {
          if (slangDict.containsKey(word))
            noSlangText.append(slangDict.get(word));
          else
            noSlangText.append(word);
          noSlangText.append(" ");
        }
        words = noSlangText.toString().split("\\s+");
        for (String word : words) {
          if (stopWordSet.contains(word))
            continue;
          clearText.append(word).append(" ");
        }
        t.setText(clearText.toString().replaceAll("[^\\w\\d\\s]", ""));
        return t;
      }
    });
  System.out.println("number of clean tweets: "+ tweetsRDD.count());
  //tweetsRDD.saveAsObjectFile("/import/dream/repos/dev/schevtso/sparkathon/twitter/cleanedTweetsRDD");
  
  //get vocabulory
  List<String> dictionary = sc.textFile(dict).collect();
  HashSet<String> dictHash = new HashSet<>(dictionary);
  //get vocabulory
  JavaRDD<String> wordVocab = tweetsRDD.flatMap(t -> Arrays.asList(t.getText().toLowerCase().split(" ")))
          .mapToPair(s -> new Tuple2<>(s, 1))
          .reduceByKey((a,b) -> { return a+b; } )
          .filter(t -> t._2 > 25)
          .filter(t -> dictHash.contains(t._1))
          .map(t -> t._1);
          
  
  List<String> vocab = wordVocab.collect();
  System.out.println("number of filtered words: "+ vocab.size());
  Utils.writeFile(vocab, outDir + "vocab.txt");
  Utils.writeFile(tagsList, outDir + "tags.txt");
  
  HashMap<String, Integer> vocabMap = new HashMap<>();
  HashMap<String, Integer> tagMap = new HashMap<>();
  for(int i=0; i<vocab.size(); i++) {
    vocabMap.put(vocab.get(i), i);
  }
  for(int i=0; i<tagsList.size(); i++) {
    tagMap.put(tagsList.get(i), i);
  }
  
  
  JavaRDD<String> labeledPoints = tweetsRDD.flatMap( t -> {
      List<String> result = new ArrayList<>();
      String tweet = t.getText().toLowerCase();
      String[] words = tweet.split(" ");
      StringBuilder sb = new StringBuilder();
      for(String w : words) {
        if(vocabMap.containsKey(w))
          sb.append(vocabMap.get(w)).append(" ");
      }
    
      for(Hashtag h : t.getEntities().getHashtags())
        if(h!=null) {
          if (tagMap.containsKey(h.getText().toLowerCase()))
            result.add(tagMap.get(h.getText().toLowerCase()) + "," + sb.toString());
        }
      return result;
      
    });
    List<String> mat = labeledPoints.collect();
    System.out.println("LP size : " + mat.size());
    Utils.writeFile(labeledPoints.collect(), outDir + "mat.txt");
  }
}
