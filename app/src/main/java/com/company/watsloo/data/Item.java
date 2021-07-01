package com.company.watsloo.data;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Item {

    private String name;
    private double latitude;
    private double longitude;
    private String description;
    private List<String> stories;
    private List<Bitmap> bitmaps;

    public Item(String name, double latitude, double longitude, String description) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        stories = new ArrayList<>();
        bitmaps = new ArrayList<>();
    }

    public Item(String name, double latitude, double longitude, String description,
                List<String> stories) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.description = description;
        this.stories = stories;
        bitmaps = new ArrayList<>();
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

    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }
}
