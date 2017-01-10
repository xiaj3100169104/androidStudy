package com.style.utils;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

public class CommonUtil {

    public static void setText(TextView textView, String str) {
        if (textView != null)
            textView.setText(getNotNullText(str));
    }

    public static String getNotNullText(String str) {
        if (TextUtils.isEmpty(str))
            return "";
        return str;
    }
}
