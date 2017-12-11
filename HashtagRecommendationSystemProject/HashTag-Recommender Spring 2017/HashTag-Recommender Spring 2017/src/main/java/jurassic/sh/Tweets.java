/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 */
package jurassic.sh;

/**
 *
 * @author gualiu
 */

import com.google.gson.annotations.SerializedName;

public class Tweets {

  @SerializedName("geo")
  public String geo;

  @SerializedName("in_reply_to_status_id")
  public String in_reply_to_status_id;

  @SerializedName("truncated")
  public String truncated;

  @SerializedName("created_at")
  public String created_at;

  @SerializedName("retweet_count")
  public String retweet_count;

  @SerializedName("in_reply_to_user_id")
  public String in_reply_to_user_id;

  @SerializedName("id_str")
  public String id_str;

  @SerializedName("place")
  public String place;

  @SerializedName("favorited")
  public boolean favorited;

  @SerializedName("source")
  public String source;

  @SerializedName("in_reply_to_screen_name")
  public String in_reply_to_screen_name;

  @SerializedName("in_reply_to_status_id_str")
  public String in_reply_to_status_id_str;

  @SerializedName("id")
  public long id;

  @SerializedName("contributors")
  public String contributors;

  @SerializedName("coordinates")
  public String coordinates;

  @SerializedName("retweeted")
  public boolean retweeted;

  @SerializedName("text")
  public String text;

  @SerializedName("profile_image_url")
  public String profile_image_url;

  // public user user;
  public User user;

}

