package com.style.app;

import android.os.Process;
import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Created by xiajun on 2017/12/22.
 */

public class AppCrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "app_crash";

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        Log.e("AppCrashHandler", "uncaughtException");

        //读取stacktrace信息
        final Writer result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        ex.printStackTrace(printWriter);
        String errorReport = result.toString();
        Log.e(TAG, errorReport);
        Process.killProcess(Process.myPid());

    }
}
