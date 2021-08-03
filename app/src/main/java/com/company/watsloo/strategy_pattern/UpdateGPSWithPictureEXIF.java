package com.company.watsloo.strategy_pattern;

import android.media.ExifInterface;

public class UpdateGPSWithPictureEXIF implements UpdateGPSBehavior{

    private String mylat;
    private String mylon;

    public UpdateGPSWithPictureEXIF(ExifInterface exifInterface){
        String lat = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
        String log = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
        String logREF = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);

        // if there is gps information in the picture, read it out
        if (lat!=null){
            mylat = String.valueOf(covertRationalGPS2DecimalGPS(lat));

            if (logREF.equals("W")){
                //need to show negative value when Longitrude is in West
                mylon = String.valueOf(-1*covertRationalGPS2DecimalGPS(log));}
            else{
                 mylon = String.valueOf(covertRationalGPS2DecimalGPS(log));
            }
//            textView_lat.setTextColor(Color.BLUE);
//            textView_lon.setTextColor(Color.BLUE);
        } else{
            // if there is no gps information in the picture, inform the user
//            Toast.makeText(this, "Please manually input the GPS information for this picture", Toast.LENGTH_LONG).show();
            mylat = "No GPS Lat Found";
            mylon = "No GPS Lon Found";
//            textView_lat.setText("None");
//            textView_lon.setText("None");
//            textView_lat.setTextColor(Color.RED);
//            textView_lon.setTextColor(Color.RED);
        }
    }

    @Override
    public String updateLat() {
        return mylat;
    }

    @Override
    public String updateLog() {
        return mylon;
    }



    // Convert the rational GPS information into the decimal gps information
    private Float covertRationalGPS2DecimalGPS(String gpsinput){
        String[] gpsList  = gpsinput.replace("/", ",").split(",");

        Float gpsD =0f;

        Float gpsM =0f;

        Float gpsS =0f;

        if (gpsList.length >=2) {

            gpsD = Float.parseFloat(gpsList[0]) / Float.parseFloat(gpsList[1]);

        }

        if (gpsList.length >=4) {

            gpsM = Float.parseFloat(gpsList[2]) / Float.parseFloat(gpsList[3]);

        }

        if (gpsList.length >=6) {

            gpsS = Float.parseFloat(gpsList[4]) / Float.parseFloat(gpsList[5]);

        }

        return  gpsD + gpsM /60 + gpsS /3600;
    }
}
