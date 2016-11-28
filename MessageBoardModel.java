package com.example.baselife.model;

import java.util.ArrayList;
import com.google.gson.annotations.SerializedName;
/**
 * Created by dhaval on 9/9/16.
 */
public class MessageBoardModel {
    // take care of inner class array list
    public MessageBoardModel() {

    }
    /*@Override
    public String toString() {
        return meta.toString()+":"+threadList.toString()+":"+profileList.toString();
    }*/
    @SerializedName("meta")
    public Meta meta =  new Meta();
    @SerializedName("threads")
    private ArrayList<Thread> threadList = new ArrayList<Thread>();

    public ArrayList<Thread> getThreadList() {
        return threadList;
    }

    public void setThreadList(ArrayList<Thread> threadList) {
        this.threadList = threadList;
    }


    @SerializedName("profiles")
    private ArrayList<Profiles> profileList = new ArrayList<Profiles>();

    public ArrayList<Profiles> getProfileList() {
        return profileList;
    }

    public void setProfileList(ArrayList<Profiles> profileList) {
        this.profileList = profileList;
    }


}
