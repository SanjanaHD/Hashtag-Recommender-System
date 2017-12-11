import simplejson as json
import oauth2 as oauth
import sys
import time
import urllib2 as urllib

api_key = "wmsUo9fpXBA2as2ofEQvsDkBv"
api_secret = "oA7m3SeSzGBTdozdAH5xMO6i1e91ZlhhA2QCbv2TALIsqyVGoW"
access_token_key = "3178808264-T4mpzKqcDt5HXYTRClGwXK6rJAW2vWpN8FpDTV1"
access_token_secret = "YfxmzSpEbtcdnzTWPpLdkK52m6GgQJ1kkTAAhPrvdr0Jt"

_debug = 0

oauth_token    = oauth.Token(key=access_token_key, secret=access_token_secret)
oauth_consumer = oauth.Consumer(key=api_key, secret=api_secret)

signature_method_hmac_sha1 = oauth.SignatureMethod_HMAC_SHA1()

http_method = "GET"

http_handler  = urllib.HTTPHandler(debuglevel=_debug)
https_handler = urllib.HTTPSHandler(debuglevel=_debug)
https_proxy_handler = urllib.ProxyHandler({'https': 'www-proxy.us.oracle.com:80'})

current_hashtag = []
max_ids = {}
'''
Construct, sign, and open a twitter request
using the hard-coded credentials above.
'''
def twitterreq(url, method, parameters):
  req = oauth.Request.from_consumer_and_token(oauth_consumer,
                                             token=oauth_token,
                                             http_method=http_method,
                                             http_url=url, 
                                             parameters=parameters)

  req.sign_request(signature_method_hmac_sha1, oauth_consumer, oauth_token)

  headers = req.to_header()

  if http_method == "POST":
    encoded_post_data = req.to_postdata()
  else:
    encoded_post_data = None
    url = req.to_url()

  opener = urllib.OpenerDirector()
  opener.add_handler(http_handler)
  opener.add_handler(https_handler)
  opener.add_handler(https_proxy_handler)

  response = opener.open(url, encoded_post_data)

  return response

def process_tweets(statuses):
  global current_hashtag
  global max_ids
  obj = json.loads(statuses)
  if "statuses" not in obj:
     return
  tweets = obj["statuses"]
  for tweet_obj in obj["statuses"]:
        id = tweet_obj["id"]
        if current_hashtag in max_ids and max_ids[current_hashtag] < id:
          pass
       	else:
          max_ids[current_hashtag] = id     
  	print "%s" % json.dumps(tweet_obj)

def fetchsamples():
  global current_hashtag
  global max_ids
  # count of 100 is max
  url = "https://api.twitter.com/1.1/search/tweets.json?q=%%23%s&result_type=recent&count=100" % current_hashtag
  if current_hashtag in max_ids:
    max_id = max_ids[current_hashtag]
    url = "%s&max_id=%d" % (url, max_id - 1)
  parameters = []
  response = twitterreq(url, "GET", parameters)
  for statuses in response:
    process_tweets(statuses)

def read_hashtags():
  hashtags = []
  f = open("hashtags.txt", "r")
  for line in f.readlines():
    ht = line.strip()[1:]
    if ht:
      hashtags.append(ht)
  f.close()
  return hashtags

if __name__ == '__main__':
  global current_hashtag
  hashtags = read_hashtags()
  for i in range(100):
    for ht in hashtags:
      current_hashtag = ht
      f = open("data2/%s.json" % current_hashtag, "a")
      sys.stdout = f
      fetchsamples()
      time.sleep(5)
      sys.stdout = sys.__stdout__
      f.close()
