package com.company.watsloo;

import android.app.Application;
import android.content.Context;

public class GlobalVariables extends Application {


    private String someString;
    private static Context context;

    public String getSomeString() {
        return someString;
    }

    public void setSomeString(String someString) {
        this.someString = someString;
    }



    public void onCreate() {
        super.onCreate();
        GlobalVariables.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return GlobalVariables.context;
    }
}
