package com.demo.bms.appdemo;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by bms on 16-12-13.
 */

public class BmsZhihuApp extends Application {
    private static BmsZhihuApp applicationContext;

    public static BmsZhihuApp getInstance() {
        return applicationContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;

    }

    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }
}
