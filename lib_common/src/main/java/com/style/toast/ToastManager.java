package com.style.toast;

import android.content.Context;
import androidx.annotation.StringRes;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastManager {

    public static void showToast(Context context, CharSequence str) {
        if (TextUtils.isEmpty(str))
            return;
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }

    public static void showToast(Context context, @StringRes int resId) {
        showToast(context, context.getText(resId));
    }

}
