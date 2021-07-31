package com.company.watsloo.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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

    @Override
    public void addItem(Context context) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference itemRef = dbRef.child(getName());

        DataOperation.addSingleField(context, itemRef, "latitude", getLatitude());
        DataOperation.addSingleField(context, itemRef, "longitude", getLongitude());
        DataOperation.addSingleField(context, itemRef, "description", getDescription());

        if (!getStories().isEmpty()) {
            DataOperation.addStories(context, getName(), getStories());
        }
    }
}
