package com.style.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo


/**
 * 网络状态工具类
 * Created by xiajun on 2017/1/9.
 */

object NetWorkUtil {

    private fun getActiveNetworkInfo(context: Context): NetworkInfo? {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return connectivityManager.activeNetworkInfo
    }

    fun isNetWorkActive(context: Context): Boolean {
        return isMobileActive(context) || isWifiActive(context)
    }

    fun isMobileActive(context: Context): Boolean {
        return isNetWorkActiveByType(context, ConnectivityManager.TYPE_MOBILE)
    }

    fun isWifiActive(context: Context): Boolean {
        return isNetWorkActiveByType(context, ConnectivityManager.TYPE_WIFI)
    }

    private fun isNetWorkActiveByType(context: Context, type: Int): Boolean {
        val activeNetworkInfo = getActiveNetworkInfo(context)
        return activeNetworkInfo != null && activeNetworkInfo.type == type
    }
}
