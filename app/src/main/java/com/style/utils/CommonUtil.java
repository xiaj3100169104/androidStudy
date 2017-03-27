package com.style.utils;

import android.text.TextUtils;
import android.widget.TextView;

import java.nio.charset.Charset;

public class CommonUtil {

    public static String getUUID() {
       String UUID = java.util.UUID.randomUUID().toString();
       return new String(UUID.getBytes(), Charset.forName("UTF-8"));
    }

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
