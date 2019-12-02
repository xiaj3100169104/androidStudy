package com.style.utils;

import android.util.Log;

import com.style.lib.common.BuildConfig;

public class LogManager {
    public static void logI(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.i(tag, msg);
    }

    public static void logE(String tag, String msg) {
        if (BuildConfig.DEBUG)
            Log.e(tag, msg);
    }
}
