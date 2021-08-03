package com.company.watsloo.strategy_pattern;

import android.content.Intent;

public class UpdateGPSWithIntent implements UpdateGPSBehavior{
    private String myGPSKey;
    private String mylat;
    private String mylon;

    public UpdateGPSWithIntent(String intent_lat, String intent_lon){


            mylat = intent_lat;
            mylon = intent_lon;
        }

    public UpdateGPSWithIntent(Intent intent){


        mylat = intent.getStringExtra("lat");
        mylon = intent.getStringExtra("log");
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
