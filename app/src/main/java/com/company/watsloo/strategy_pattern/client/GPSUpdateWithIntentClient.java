package com.company.watsloo.strategy_pattern.client;

import android.content.Intent;
import android.graphics.Color;
import android.widget.TextView;

import com.company.watsloo.strategy_pattern.UpdateGPSBehavior;
import com.company.watsloo.strategy_pattern.UpdateGPSWithIntent;

public class GPSUpdateWithIntentClient extends GPSUpdatreManager{

    private Intent intent;
    private UpdateGPSBehavior updateGPSBehavior;

    public GPSUpdateWithIntentClient(TextView textViewLat, TextView textViewLon, Intent intent) {
        super(textViewLat, textViewLon);
        // make a new strategy instance
        updateGPSBehavior = new UpdateGPSWithIntent(this.intent);
        // set the strategy
        this.setUpdateGPSBehavior(updateGPSBehavior);
        // perform the action
        this.performUpdateGPS();
        this.getMyLatTextview().setTextColor(Color.GREEN);
        this.getMyLonTextview().setTextColor(Color.GREEN);
    }

}
