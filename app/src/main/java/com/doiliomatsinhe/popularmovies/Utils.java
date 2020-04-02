package com.doiliomatsinhe.popularmovies;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class Utils {

    public static boolean isAppInstalled(Context context, String appName) {
        PackageManager packageManager = context.getPackageManager();
        boolean installed = false;
        List<PackageInfo> packages = packageManager.getInstalledPackages(0);
        for (PackageInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(appName)) {
                installed = true;
                break;
            }

        }

        return installed;
    }
}
