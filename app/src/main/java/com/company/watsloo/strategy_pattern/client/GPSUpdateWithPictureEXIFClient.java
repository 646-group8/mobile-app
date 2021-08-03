package com.company.watsloo.strategy_pattern.client;

import android.graphics.Color;
import android.media.ExifInterface;
import android.widget.TextView;

import com.company.watsloo.strategy_pattern.UpdateGPSBehavior;
import com.company.watsloo.strategy_pattern.UpdateGPSWithGPSValue;
import com.company.watsloo.strategy_pattern.UpdateGPSWithPictureEXIF;

public class GPSUpdateWithPictureEXIFClient extends GPSUpdatreManager{
    private ExifInterface exifInterface;
    private UpdateGPSBehavior updateGPSBehavior;

    public GPSUpdateWithPictureEXIFClient(TextView textViewLat, TextView textViewLon, ExifInterface exifInterface) {
        super(textViewLat, textViewLon);
        this.exifInterface = exifInterface;
        // make a new strategy instance
        updateGPSBehavior = new UpdateGPSWithPictureEXIF(this.exifInterface);
        // set the strategy
        this.setUpdateGPSBehavior(updateGPSBehavior);
        // perform the action
        this.performUpdateGPS();
        this.getMyLatTextview().setTextColor(Color.YELLOW);
        this.getMyLonTextview().setTextColor(Color.YELLOW);

    }
}
