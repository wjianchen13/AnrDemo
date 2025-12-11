package com.example.anrdemo;

import android.util.Log;

public class Utils {

    public static final String TAG = "block";

    public static void log(String log) {
        log(TAG, log);
    }

    public static void log(String tag, String log) {
        Log.d(tag, "=======================> " + log);
    }

}
