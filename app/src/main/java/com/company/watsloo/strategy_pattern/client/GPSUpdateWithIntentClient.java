package com.company.watsloo.strategy_pattern.client;

import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import com.company.watsloo.strategy_pattern.UpdateGPSBehavior;
import com.company.watsloo.strategy_pattern.UpdateGPSWithIntent;

public class GPSUpdateWithIntentClient extends GPSUpdatreManager{

    private String intent_lat;
    private String intent_lon;
    private UpdateGPSBehavior updateGPSBehavior;

    public GPSUpdateWithIntentClient(TextView textViewLat, TextView textViewLon, String mylat, String mylong) {
        super(textViewLat, textViewLon);
        // make a new strategy instance
        updateGPSBehavior = new UpdateGPSWithIntent(mylat,mylong);
        // set the strategy
        this.setUpdateGPSBehavior(updateGPSBehavior);
        // perform the action
        this.performUpdateGPS();
        this.getMyLatTextview().setTextColor(Color.GREEN);
        this.getMyLonTextview().setTextColor(Color.GREEN);
    }

    public GPSUpdateWithIntentClient(TextView textViewLat, TextView textViewLon, Intent intent) {
        super(textViewLat, textViewLon);
        // make a new strategy instance
        updateGPSBehavior = new UpdateGPSWithIntent(intent);
        // set the strategy
        this.setUpdateGPSBehavior(updateGPSBehavior);
        // perform the action
        this.performUpdateGPS();
        this.getMyLatTextview().setTextColor(Color.GREEN);
        this.getMyLonTextview().setTextColor(Color.GREEN);
    }

}
