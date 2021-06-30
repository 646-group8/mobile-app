package com.company.watsloo.data;

import android.media.Image;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private String name;
    private double latitude;
    private double longitude;
    private String description;
    private List<String> stories;
    private List<Image> images;

    public Item(String name, double latitude, double longitude, String description) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        stories = new ArrayList<>();
        images = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getStories() {
        return stories;
    }

    public List<Image> getImages() {
        return images;
    }
}
