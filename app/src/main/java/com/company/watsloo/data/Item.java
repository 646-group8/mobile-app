package com.company.watsloo.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class Item implements ItemInterface {

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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getLatitude() {
        return latitude;
    }

    @Override
    public double getLongitude() {
        return longitude;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public List<String> getStories() {
        return stories;
    }

    @Override
    public List<Bitmap> getBitmaps() {
        return bitmaps;
    }
}
