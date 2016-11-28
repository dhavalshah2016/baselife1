package com.example.baselife.model;

/**
 * Created by dhaval on 22/8/16.
 */
public class BaseModel {
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    private int id;
    private String name;
    private String city;
    private String state;

    public String getBackground_photo_url() {
        return background_photo_url;
    }

    public void setBackground_photo_url(String background_photo_url) {
        this.background_photo_url = background_photo_url;
    }

    private String background_photo_url;
    private double lat;
    private double lng;
    private int group;
}
