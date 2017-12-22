package com.style.utils;

import android.text.TextUtils;
import android.widget.TextView;

import java.nio.charset.Charset;

public class CommonUtil {

    public static String getUUID() {
       String UUID = java.util.UUID.randomUUID().toString();
       return new String(UUID.getBytes(), Charset.forName("UTF-8"));
    }

    public static CharSequence getNotNullText(CharSequence str) {
        return str == null ? "" : str;
    }
}
