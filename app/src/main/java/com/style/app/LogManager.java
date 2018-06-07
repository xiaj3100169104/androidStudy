package com.style.app;

import android.util.Log;

import com.style.framework.BuildConfig;

public class LogManager {
    public static void logI(String tag, String msg) {
        if (BuildConfig.LOG_ENABLE)
            Log.i(tag, msg);
    }

    public static void logE(String tag, String msg) {
        if (BuildConfig.LOG_ENABLE)
            Log.e(tag, msg);
    }
}
