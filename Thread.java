package com.example.baselife.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by dhaval on 22/9/16.
 */
public  class Thread {
   /* @Override
    public String toString() {
        return id+":"+posted_by_profile+":"+posted_anonymously+":"+number_of_replies+":"+LastReplyList.toString()+":"+FirstPostList.toString();
    }*/
    @SerializedName("id")
    private int id;
    @SerializedName("posted_by_profile")
    private int posted_by_profile;

    public boolean isPosted_anonymously() {
        return posted_anonymously;
    }

    public void setPosted_anonymously(boolean posted_anonymously) {
        this.posted_anonymously = posted_anonymously;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosted_by_profile() {
        return posted_by_profile;
    }

    public void setPosted_by_profile(int posted_by_profile) {
        this.posted_by_profile = posted_by_profile;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public int getNumber_of_replies() {
        return number_of_replies;
    }

    public void setNumber_of_replies(int number_of_replies) {
        this.number_of_replies = number_of_replies;
    }
    @SerializedName("posted_anonymously")
    private boolean posted_anonymously;


    @SerializedName("group")
    private int group;
    @SerializedName("first_post")
    public FirstPost firstPost =  new FirstPost();

    @SerializedName("last_reply")
    public LastReply lastReply =  new LastReply();



    @SerializedName("number_of_replies")
    private int number_of_replies;
}
