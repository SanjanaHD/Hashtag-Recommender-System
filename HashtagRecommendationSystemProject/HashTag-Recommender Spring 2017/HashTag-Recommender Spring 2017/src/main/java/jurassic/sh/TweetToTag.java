/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 */
package jurassic.sh;
/**
 *
 * @author gualiu
 */

import com.google.gson.Gson;
import com.twitter.Hashtag;
import com.twitter.Tweet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.spark.api.java.function.PairFlatMapFunction;
import scala.Tuple2;


public class TweetToTag implements PairFlatMapFunction<String,String,List<Tuple2<String, Integer>>> {
    
    public Iterable<Tuple2<String, List<Tuple2<String, Integer>>>> call(String s) {
        
        //          Tag                 KeyWord KeyWordCount    
        List<Tuple2<String, List<Tuple2<String, Integer>>>> tags = new ArrayList<>();
      try {
          Gson gson = new Gson();
          Tweet t = gson.fromJson(s, Tweet.class);
          
          
          String text = t.getText();
          String[] words = text.split(" ");
          HashMap<String, Integer> countMap = new HashMap<>();
          
          for(String w: words) {
              w = w.trim();
              if(w.startsWith("#") || w.equalsIgnoreCase("the") || w.equalsIgnoreCase("a") || w.equalsIgnoreCase("is") || w.equalsIgnoreCase("was"))
                  continue;
              
             if(countMap.containsKey(w))
                 countMap.put(w, countMap.get(w)+1);
             else
                 countMap.put(w, 1);
          }
          
          // Map to list
          List<Tuple2<String, Integer>> countList = new ArrayList<>();
          for(String k: countMap.keySet())
              countList.add(new Tuple2<>(k, countMap.get(k)));
          
          for(Hashtag h : t.getEntities().getHashtags())
              if(h!=null && h.getText()!=null)
                tags.add(new Tuple2<>(h.getText(),countList));
       //Category c= TwitterObjectFactory.createCategory(s);
        //  System.out.println("****" + c.getName());
          
      } catch(Exception e) {
          
          
      }
        
        return tags;
    }

}
