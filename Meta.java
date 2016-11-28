package com.example.baselife.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dhaval on 22/9/16.
 */
public class Meta {
   /* @Override
    public String toString() {
        return count+":"+page;
    }*/
    @SerializedName("count")
    private int count;
    @SerializedName("page")
    private int page;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
