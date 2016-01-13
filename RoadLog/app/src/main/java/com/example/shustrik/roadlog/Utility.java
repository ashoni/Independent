package com.example.shustrik.roadlog;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.widget.Toast;


public abstract class Utility {
    //Called on main fragment create
    public static boolean checkCameraExists(Activity activity) {
        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    //get period from preferences
    public static long getPicturesPeriod(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        long minutes = prefs.getLong("pref_interval", 30);
        return minutes * 60 * 1000;
    }

    //true for back camera, false for front
    public static boolean getCameraType(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("pref_camera", 0) == 0;
    }

    public static int getFocusMode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("pref_focus", 0);
    }

    public static int getPictureAmount(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt("pref_focus", 0);
    }

    public static String getAlbumName(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString("album_name", "RoadLog");
    }
}
