package com.example.baselife.model;

/**
 * Created by dhaval on 11/9/16.
 */
public class GroupModel {
    public GroupModel()
    {

    }
    private int id;
    private String name;
    private int parentGroup;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentGroup() {
        return parentGroup;
    }

    public void setParentGroup(int parentGroup) {
        this.parentGroup = parentGroup;
    }

    public int getIsSubscribedTo() {
        return isSubscribedTo;
    }

    public void setIsSubscribedTo(int isSubscribedTo) {
        this.isSubscribedTo = isSubscribedTo;
    }

    private int isSubscribedTo;
}
