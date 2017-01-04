package com.style.newwork.common;

import android.util.Log;
import android.widget.Toast;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;

public class NetStringCallback extends StringCallback {
    protected String TAG = "NetStringCallback";


    @Override
    public void onError(Call call, Exception e, int id) {

    }

    @Override
    public void onResponse(String response, int id) {

    }
}
