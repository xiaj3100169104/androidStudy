package com.style.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.style.manager.ToastManager;
import com.style.utils.NetWorkUtil;

import xj.mqtt.manager.IMManagerImpl;

/**
 * Created by xiajun on 2017/1/9.
 * getNetworkInfo(int) was deprecated in 23.
 * The suggestion was to use getAllNetworks and getNetworkInfo(Network) instead.
 * However both of these require minimum of API 21.
 */

public class NetWorkChangeBroadcastReceiver extends BroadcastReceiver {
    public static final String TAG = "NetWorkChangeBroadcastReceiver";
    public static final String NET_CHANGE = "net_change";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetWorkUtil.isNetWorkActive(context))
            ToastManager.showToast(context, "网络不可用");
        else
            IMManagerImpl.getInstance().connect();
    }
}
