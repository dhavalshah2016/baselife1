package com.example.baselife.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhaval on 22/9/16.
 */
public  class Profiles {
    @SerializedName("id")
    private int id;
    @SerializedName("username")
    private String userName;
    @SerializedName("is_verified")
    private boolean isVerified;
    @SerializedName("display_name")
    private String displayName;
    @SerializedName("image_url")
    private String imgUrl;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("rank")
    private String rank;
    /*@Override
    public String toString() {
        return id+":"+userName+":"+isVerified+":"+displayName+":"+imgUrl+":"+firstName+":"+lastName+":"+rank;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }
}
