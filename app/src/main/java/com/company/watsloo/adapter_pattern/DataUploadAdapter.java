package com.company.watsloo.adapter_pattern;

import android.content.Context;
import android.graphics.Bitmap;

import com.company.watsloo.data.Item;
import com.company.watsloo.data.ItemInterface;
import com.company.watsloo.data.ItemWithContactInfo;
import com.company.watsloo.data.ItemWithEmail;

import java.util.ArrayList;
import java.util.List;

public class DataUploadAdapter {

    private ItemInterface myItem;

    public DataUploadAdapter(String name, String lat, String log, String discription, String story, Bitmap bitmap){
        double latitude = Double.parseDouble(lat);
        double longtitude = Double.parseDouble(log);
        List<String> storyList = new ArrayList<>();
        List<Bitmap> bitmapList = new ArrayList<>();
        story +="$$$$$";
        storyList.add(story);
        bitmapList.add(bitmap);
        Item itemWrapee = new Item(name, latitude,longtitude,discription, storyList,bitmapList);
        this.myItem = itemWrapee;
    }

    public DataUploadAdapter(String name, String lat, String log, String discription, String story, Bitmap bitmap,String email){
        this( name,  lat,  log,  discription,  story, bitmap);
        if (!email.equals(null)) {
            myItem = new ItemWithEmail(myItem, email);
        }
    }

    public void ItemUpload(Context context){
        myItem.addItem(context);
    }
}
