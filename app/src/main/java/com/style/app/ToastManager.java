package com.style.app;

import android.content.Context;
import android.support.annotation.StringRes;
import android.util.Log;
import android.widget.Toast;

import com.style.framework.BuildConfig;

public class ToastManager {

    public static void showToast(Context context, CharSequence str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, @StringRes int resId) {
        showToast(context, context.getText(resId));
    }

}
