package com.company.watsloo.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Item implements ItemInterface {

    private String name;
    private double latitude;
    private double longitude;
    private String description;
    private List<String> stories;
    private List<Bitmap> bitmaps;
    private boolean isAudited = false;
    private boolean isEasterEgg = false;

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

    @Override
    public boolean isAudited() {
        return isAudited;
    }

    @Override
    public boolean isEasterEgg() {
        return isEasterEgg;
    }

    @Override
    public void addItem(Context context) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference itemRef = dbRef.child(getName());

        DataOperation.addSingleField(context, itemRef, "latitude", getLatitude());
        DataOperation.addSingleField(context, itemRef, "longitude", getLongitude());
        DataOperation.addSingleField(context, itemRef, "description", getDescription());
        DataOperation.addSingleField(context, itemRef, "isAudited", false);
        DataOperation.addSingleField(context, itemRef, "isEasterEgg", false);

        if (!getStories().isEmpty()) {
            DataOperation.addStories(context, getName(), getStories());
        }

//        Toast.makeText(context, "Data uploaded.", Toast.LENGTH_SHORT).show();
    }
}
