package com.style.app;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;

import com.style.view.systemHelper.SmartToast;


public class CustomToastManager {
    private final String TAG = getClass().getSimpleName();
    private final static Handler mHandler = new Handler(Looper.getMainLooper());


    public static void showInfo(Context context, @StringRes int resId) {
        showInfo(context, context.getString(resId));
    }

    public static void showInfo(Context context, String message) {
        runOnUiThread(() -> {
            SmartToast mToast = new SmartToast(context);
            mToast.setType(SmartToast.INFO);
            mToast.setMessage(message);
            mToast.show();
        });
    }

    public static void showSuccess(Context context, @StringRes int resId) {
        showSuccess(context, context.getString(resId));
    }

    public static void showSuccess(Context context, String message) {
        runOnUiThread(() -> {
            SmartToast mToast = new SmartToast(context);
            mToast.setType(SmartToast.SUCCESS);
            mToast.setMessage(message);
            mToast.show();
        });
    }

    public static void showError(Context context, @StringRes int resId) {
        showError(context, context.getString(resId));
    }

    public static void showError(Context context, String message) {
        runOnUiThread(() -> {
            SmartToast mToast = new SmartToast(context);
            mToast.setType(SmartToast.ERROR);
            mToast.setMessage(message);
            mToast.show();
        });
    }

    public static void runOnUiThread(Runnable action) {
        if (Looper.myLooper() == Looper.getMainLooper()) { // UI主线程
            action.run();
        } else { // 非UI主线程
            //Looper.prepare();
            mHandler.post(action);
            //Looper.loop();
        }
    }
}
