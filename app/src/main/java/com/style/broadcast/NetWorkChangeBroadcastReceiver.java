package com.style.broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.style.app.ToastManager;
import com.style.utils.NetWorkUtil;

/**
 * Created by xiajun on 2017/1/9.
 * getNetworkInfo(int) was deprecated in 23.
 * The suggestion was to use getAllNetworks and getNetworkInfo(Network) instead.
 * However both of these require minimum of API 21.
 */

public class NetWorkChangeBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "NetWorkChangeBroadcastReceiver";
    public static final String NET_CHANGE = "net_change";

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetWorkUtil.isNetWorkActive(context))
            ToastManager.showToast(context,"网络不可用");
        else
            Log.e(TAG, "网络连接");

    }
}
