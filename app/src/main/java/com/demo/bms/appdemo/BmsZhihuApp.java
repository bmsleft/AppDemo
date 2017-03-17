package com.demo.bms.appdemo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.demo.bms.appdemo.db.DailyNewsDataSource;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by bms on 16-12-13.
 */

public class BmsZhihuApp extends Application {
    private static BmsZhihuApp applicationContext;
    private static DailyNewsDataSource dataSource;

    public static BmsZhihuApp getInstance() {
        return applicationContext;
    }
    public static DailyNewsDataSource getDataSource() { return dataSource; }

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this;

        initImageLoder(getApplicationContext());

    }

    public static void initImageLoder(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .denyCacheImageMultipleSizesInMemory()
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }

    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }
}
