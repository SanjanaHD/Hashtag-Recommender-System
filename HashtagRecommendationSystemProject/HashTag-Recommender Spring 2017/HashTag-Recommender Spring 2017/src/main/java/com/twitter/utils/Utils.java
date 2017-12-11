/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.twitter.utils;

import java.io.PrintWriter;
import java.util.List;

/**
 *
 * @author gualiu
 */
public class Utils {
  
  public static void writeFile(List<String> list, String fileName) {
    
    
    try {
      PrintWriter writer = new PrintWriter(fileName, "UTF-8");
    
      for(String s: list)
        writer.println(s);

      writer.close();
    } catch(Exception e) {
      
    }
  }
  
  
  
  
  
  
}
