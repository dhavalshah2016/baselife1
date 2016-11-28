package com.example.baselife.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dhaval on 22/9/16.
 */
public  class LastReply {
    /*@Override
    public String toString() {
        return id+":"+created_date+":"+posted_anonymously+":"+posted_by_profile+":"+thread+":"+text+":"+mediaList.toString()+":"+media;
    }*/
    @SerializedName("id")
    private int id;
    @SerializedName("created_date")
    private String created_date;
    @SerializedName("posted_by_profile")
    private int posted_by_profile;
    @SerializedName("posted_anonymously")
    private boolean posted_anonymously;
    @SerializedName("thread")
    private int thread;
    @SerializedName("text")
    private String text;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public int getPosted_by_profile() {
        return posted_by_profile;
    }

    public void setPosted_by_profile(int posted_by_profile) {
        this.posted_by_profile = posted_by_profile;
    }

    public boolean isPosted_anonymously() {
        return posted_anonymously;
    }

    public void setPosted_anonymously(boolean posted_anonymously) {
        this.posted_anonymously = posted_anonymously;
    }

    public int getThread() {
        return thread;
    }

    public void setThread(int thread) {
        this.thread = thread;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<MediaObjects> getMediaList() {
        return mediaList;
    }

    public void setMediaList(ArrayList<MediaObjects> mediaList) {
        this.mediaList = mediaList;
    }

    /*  public ArrayList<Integer> getMediaIntList() {
          return mediaIntList;
      }

      public void setMediaIntList(ArrayList<Integer> mediaIntList) {
          this.mediaIntList = mediaIntList;
      }*/
    @SerializedName("media")
    private ArrayList<Integer> media;

    public ArrayList<Integer> getMedia() {
        return media;
    }

    public void setMedia(ArrayList<Integer> media) {
        this.media = media;
    }

    @SerializedName("media_objects")
    private ArrayList<MediaObjects> mediaList = new ArrayList<MediaObjects>();

    // private ArrayList<Integer> mediaIntList = new ArrayList<Integer>();
}
