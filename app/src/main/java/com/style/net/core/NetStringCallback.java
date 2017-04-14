package com.style.net.core;

import android.util.Log;
import com.style.manager.ToastManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import test.app.MyApp;


public class NetStringCallback implements Callback<String> {
    protected String TAG = "NetStringCallback";

    public void onResponse(Call<String> call, Response<String> response) {
        Log.e(TAG, "response.body==" + response.body());
    }

    @Override
    public void onFailure(Call<String> call, Throwable t) {
        ToastManager.showToast(MyApp.getAppContext(),"请求错误");
    }


}
