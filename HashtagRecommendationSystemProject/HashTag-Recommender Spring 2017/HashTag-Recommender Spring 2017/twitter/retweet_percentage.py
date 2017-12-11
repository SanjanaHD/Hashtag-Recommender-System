import os
import simplejson as json

def count_retweets():
  SRC_DIR_PATH = 'data2'
  hashtag_retweets = {}
  for fname in os.listdir(SRC_DIR_PATH):
    retweet_count = 0
    tweet_count = 0
    f = open("%s/%s" % (SRC_DIR_PATH, fname), "r")
    for line in f.readlines():
      obj = json.loads(line)
      if obj["retweet_count"] > 0:
     	retweet_count = retweet_count + 1
      tweet_count = tweet_count + 1
    hashtag_retweets[fname[:-5]] = float(retweet_count) / float(tweet_count) 
    print "%s %f" % (fname[:-5], hashtag_retweets[fname[:-5]])
    f.close()
  
  f_src = open("final_Input/tags.txt", "r")
  f_dest = open("final_Input/retweet_percentages.txt", "w")
  for line in f_src.readlines():
    line = line.strip()
    f_dest.write("%f\n" % hashtag_retweets[line])
  f_src.close()
  f_dest.close()
   

if __name__ == '__main__':
  count_retweets()
