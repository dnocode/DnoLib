package com.dnocode.lib.business.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.File;

/**
 * Created by dnocode on 03/04/15.
 */
public class AppInfo {

    private  static final String sTag="AppInfo";

    public enum AppStart { FIRST_TIME, FIRST_TIME_VERSION, NORMAL}

    private static final String LAST_APP_VERSION = "last_app_version";



    public static AppStart checkAppStart(Context context) {

        PackageInfo pInfo;

        SharedPreferences sharedPreferences = PreferenceManager .getDefaultSharedPreferences(context);

        AppStart appStart = AppStart.NORMAL;

        try {
                pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

                int lastVersionCode = sharedPreferences.getInt(LAST_APP_VERSION, -1);

                int currentVersionCode = pInfo.versionCode;



                if (lastVersionCode == -1) {

                appStart= AppStart.FIRST_TIME;

                } else if (lastVersionCode < currentVersionCode) {

                 appStart= AppStart.FIRST_TIME_VERSION;

               } else if (lastVersionCode > currentVersionCode) {

                Log.w(sTag, "Current version code (" + currentVersionCode+ ") is less then the one recognized on last startup (" + lastVersionCode+ "). Defenisvely assuming normal app start.");

                appStart= AppStart.NORMAL;

              } else {

                appStart= AppStart.NORMAL;
             }

               sharedPreferences.edit().putInt(LAST_APP_VERSION, currentVersionCode).commit();

        } catch (PackageManager.NameNotFoundException e) {

            Log.w(sTag,"Unable to determine current app version from pacakge manager. Defenisvely assuming normal app start.");
        }
        return appStart;
    }




    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int version(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {

            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }



    public static void clearData(Context context) {

        File cache = context.getCacheDir();
        File appDir = new File(cache.getParent());

        if(appDir.exists()){
            String[] children = appDir.list();
            for(String s : children){
                if(!s.equals("lib")){
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "**************** File /data/data/APP_PACKAGE/" + s + " DELETED *******************");
                }
            }
        }
    }

    public   static boolean deleteDir(File dir) {

        if (dir != null && dir.isDirectory()) {

            String[] children = dir.list();

            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

}
