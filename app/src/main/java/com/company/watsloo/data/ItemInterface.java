package com.company.watsloo.data;

import android.graphics.Bitmap;

import java.util.List;

public interface ItemInterface {
    String getName();
    double getLatitude();
    double getLongitude();
    String getDescription();
    List<String> getStories();
    List<Bitmap> getBitmaps();
}
