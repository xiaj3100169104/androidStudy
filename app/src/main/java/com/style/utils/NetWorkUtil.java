package com.style.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


/**
 * 网络状态工具类
 * Created by xiajun on 2017/1/9.
 */

public class NetWorkUtil {

    private static NetworkInfo getActiveNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo;
    }

    public static boolean isNetWorkActive(Context context) {
        if (isMobileActive(context) || isWifiActive(context)) {
            return true;
        }
        return false;
    }

    public static boolean isMobileActive(Context context) {
        return isNetWorkActiveByType(context, ConnectivityManager.TYPE_MOBILE);
    }

    public static boolean isWifiActive(Context context) {
        return isNetWorkActiveByType(context, ConnectivityManager.TYPE_WIFI);
    }

    private static boolean isNetWorkActiveByType(Context context, int type) {
        NetworkInfo activeNetworkInfo = getActiveNetworkInfo(context);
        if (activeNetworkInfo != null && activeNetworkInfo.getType() == type) {
            return true;
        }
        return false;
    }
}
