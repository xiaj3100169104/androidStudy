package com.style.newwork.common;

import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.style.manager.AccountManager;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;


public class NetDataBeanCallback<T> extends StringCallback {
    protected String TAG = getClass().getSimpleName();
    protected Class<T> clazz;
    protected TypeReference<T> type;

    public NetDataBeanCallback() {

    }

    public NetDataBeanCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public NetDataBeanCallback(TypeReference<T> type) {
        this.type = type;
    }

    @Override
    public void onError(Call call, Exception e, int id) {

    }
    @Override
    public void onResponse(String response, int id) {
        Log.e(TAG, "result==" + response);
        NetDataBean netDataBean = JSON.parseObject(response, NetDataBean.class);
        int code = netDataBean.code;
        String jsonData = netDataBean.data;
        String msg = netDataBean.msg;

        T data = null;
        if (this.clazz != null)
            data = JSON.parseObject(jsonData, this.clazz);
        if (this.type != null)
            data = JSON.parseObject(jsonData, this.type);

        if (code == NetDataBean.SUCCESS) {
            onCodeSuccess();
            onCodeSuccess(data);
            onCodeSuccess(data, msg);
        } else {
            onCodeFailure(msg);
            onCodeFailure(code, msg);
            onCodeFailure(code, data);
            if (code == NetDataBean.SERVER_ERROR) {
                //ToastUtil.showToast("服务器出错了", Toast.LENGTH_SHORT);
            } else if (code == NetDataBean.TOKEN_EXPIRED || code == NetDataBean.TOKEN_INVALID) {
                handleTokenError();
            }
        }
    }

    protected void onCodeSuccess() {

    }
    protected void onCodeSuccess(T data) {

    }

    protected void onCodeSuccess(T data, String msg) {

    }

    protected void onCodeFailure(String msg) {

    }

    protected void onCodeFailure(int code, String msg) {

    }
    protected void onCodeFailure(int code, T data) {

    }
    private void handleTokenError() {
       // AccountManager.getInstance().logOut();
    }
}
