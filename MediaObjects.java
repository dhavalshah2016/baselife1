package com.example.baselife.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhaval on 22/9/16.
 */
public  class MediaObjects {
    @SerializedName("id")
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMedia_type() {
        return media_type;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }
    @SerializedName("url")
    private String url;
    @SerializedName("media_type")
    private String media_type;
   /* @Override
    public String toString() {
        return id+":"+url+":"+media_type;
    }*/
}
