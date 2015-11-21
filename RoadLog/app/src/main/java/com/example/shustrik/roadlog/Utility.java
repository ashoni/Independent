package com.example.shustrik.roadlog;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.view.SurfaceHolder;
import android.widget.Toast;


public abstract class Utility {
    //Surface from widget
    public static SurfaceHolder activateWidget() {
        return null;
    }

    public static void deactivateWidget() {}

    //Called on main fragment create
    public static boolean checkCameraExists(Activity activity) {
        return activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    //get period from preferences
    public static long getPicturesPeriod(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getLong(context.getString(R.string.picture_interval), 5000);
    }
}
