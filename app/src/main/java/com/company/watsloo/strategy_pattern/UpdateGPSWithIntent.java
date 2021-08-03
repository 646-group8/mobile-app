package com.company.watsloo.strategy_pattern;

import android.content.Intent;

public class UpdateGPSWithIntent implements UpdateGPSBehavior{
    private String myGPSKey;
    private String mylat;
    private String mylon;

    public UpdateGPSWithIntent(Intent intent){
        myGPSKey = intent.getStringExtra("requestCode");
        if (myGPSKey!=null && myGPSKey.equals("READ_GPS_FROM_MAP")){

            mylat = intent.getStringExtra("lat");
            mylon = intent.getStringExtra("log");
        }
    }
    @Override
    public String updateLat() {
        // implements the update latitude with the pre-saved latitude information from intent
        return mylat;
    }

    @Override
    public String updateLog() {
        // implements the update longtitute with the pre-saved longtitute information from intent
        return mylon;

    }
}
