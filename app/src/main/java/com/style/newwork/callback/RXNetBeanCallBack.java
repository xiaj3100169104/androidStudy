package com.style.newwork.callback;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.style.newwork.core.NetJsonResult;
import com.style.rxAndroid.BaseRXTaskCallBack;

/**
 * Created by xiajun on 2016/10/8.
 */
public  class RXNetBeanCallBack<T> extends BaseRXTaskCallBack {
    protected String TAG = "RXNetBeanCallBack";
    protected Class<T> clazz;

    public RXNetBeanCallBack(Class<T> clazz) {
        this.clazz = clazz;
    }

    public Object doInBackground(){
        Log.e(TAG, "doInBackground");
        return null;
    }

    @Override
    public void onNextOnUIThread(Object data) {
        Log.e(TAG, "onNextOnUIThread");
        NetJsonResult result = (NetJsonResult) data;
        if (clazz != null) {
            onSuccess(JSON.parseObject(result.getData(), clazz));
        }
    }


    @Override
    public void onSuccess(Object object) {

    }

    @Override
    public void onFailed(String message) {

    }
}
