package com.style.data.app;

import android.util.Log;

import com.style.AssembleConfig;

public class LogManager {
    public static void logI(String tag, String msg) {
        if (AssembleConfig.LOG_ENABLE)
            Log.i(tag, msg);
    }

    public static void logE(String tag, String msg) {
        if (AssembleConfig.LOG_ENABLE)
            Log.e(tag, msg);
    }
}
