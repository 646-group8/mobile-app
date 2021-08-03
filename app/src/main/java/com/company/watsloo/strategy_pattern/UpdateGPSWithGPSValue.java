package com.company.watsloo.strategy_pattern;

public class UpdateGPSWithGPSValue implements UpdateGPSBehavior{

    private String mylat;
    private String mylon;

    public UpdateGPSWithGPSValue(String lat, String lon){
        mylat = lat;
        mylon = lon;

    }
    @Override
    public String updateLat() {
        // implements the update latitude with the a known GPS value
        return mylat;
    }

    @Override
    public String updateLog() {
        // implements the update latitude with the a known GPS value
        return mylon;
    }
}
