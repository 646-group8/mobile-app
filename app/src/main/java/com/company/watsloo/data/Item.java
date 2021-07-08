package com.company.watsloo.data;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;

import androidx.annotation.NonNull;

import com.company.watsloo.GlobalVariables;
import com.company.watsloo.ReadDataActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Item extends Application {

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

    public static void  readPosData(){
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String myString = "";
                for (DataSnapshot ds: snapshot.getChildren()){
                    String ps = "===========================\n";
                    // for every place, k, find the GPS information:
                    String k = ds.getKey();
                    String lat = snapshot.child(k).child("latitude").getValue(Float.class).toString();
                    String log = snapshot.child(k).child("longitude").getValue(Float.class).toString();
                    myString += k + "\n"+ lat+"\n" + log + "\n"+"======================================"+"\n" ;
                }
                //Set the global Variable
                ((GlobalVariables) GlobalVariables.getAppContext()).setSomeString(myString);
                //Need to put a thread here
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
            }
        });

    }
}
