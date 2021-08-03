package com.company.watsloo.strategy_pattern.client;

import android.widget.TextView;

import com.company.watsloo.strategy_pattern.UpdateGPSBehavior;

public class GPSUpdatreManager {
    private UpdateGPSBehavior updateGPSBehavior;
    private TextView myLatTextview;
    private TextView myLonTextview;

    public GPSUpdatreManager(TextView textViewLat, TextView textViewLon){
        this.myLatTextview = textViewLat;
        this.myLonTextview = textViewLon;
    }

    protected TextView getMyLatTextview() {
        return myLatTextview;
    }

    protected TextView getMyLonTextview() {
        return myLonTextview;
    }

    public void setUpdateGPSBehavior(UpdateGPSBehavior updateGPSBehavior) {
        this.updateGPSBehavior = updateGPSBehavior;
    }

    public void performUpdateGPS(){
        myLatTextview.setText(this.updateGPSBehavior.updateLat());
        myLonTextview.setText(this.updateGPSBehavior.updateLog());
    }

}
