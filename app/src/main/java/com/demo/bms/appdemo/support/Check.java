package com.demo.bms.appdemo.support;

import android.content.Intent;
import android.content.pm.PackageManager;

import com.demo.bms.appdemo.BmsZhihuApp;

/**
 * Created by bms on 16-12-14.
 */

public final class Check {
    private Check(){

    }

    public static boolean isAppInstalled() {
        try {
            return preparePackageManager().getPackageInfo(Constants.Information.ZHIHU_PACKAGE_ID,
                    PackageManager.GET_ACTIVITIES) != null;
        }
        catch (PackageManager.NameNotFoundException ignored) {
            return false;
        }
    }

    public static boolean isIntentSafe(Intent intent) {
        return preparePackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

    private static PackageManager preparePackageManager() {
        return BmsZhihuApp.getInstance().getPackageManager();
    }


}
