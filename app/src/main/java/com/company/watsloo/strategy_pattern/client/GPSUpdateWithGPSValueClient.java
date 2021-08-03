package com.company.watsloo.strategy_pattern.client;

import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import com.company.watsloo.strategy_pattern.UpdateGPSBehavior;
import com.company.watsloo.strategy_pattern.UpdateGPSWithGPSValue;
import com.company.watsloo.strategy_pattern.UpdateGPSWithIntent;

public class GPSUpdateWithGPSValueClient extends GPSUpdatreManager {

    private String lat, lon;
    private UpdateGPSBehavior updateGPSBehavior;

    public GPSUpdateWithGPSValueClient(TextView textViewLat, TextView textViewLon, String lat, String lon) {
        super(textViewLat, textViewLon);
        this.lat = lat;
        this.lon = lon;
        // make a new strategy instance
        updateGPSBehavior = new UpdateGPSWithGPSValue(this.lat, this.lon);
        // set the strategy
        this.setUpdateGPSBehavior(updateGPSBehavior);
        // perform the action
        this.performUpdateGPS();
        this.getMyLatTextview().setTextColor(Color.RED);
        this.getMyLonTextview().setTextColor(Color.RED);
    }
}
