package com.style.rxAndroid;

import android.util.Log;

import com.style.rxAndroid.BaseRXTaskCallBack;

/**
 * Created by xiajun on 2016/10/8.
 */
public  class RXOtherCallBack extends BaseRXTaskCallBack {
    protected String TAG = "RXOtherCallBack";

    public Object doInBackground(){
        Log.e(TAG, "doInBackground");
        return null;
    }

    @Override
    public void onNextOnUIThread(Object object) {
        onSuccess(object);
    }


    @Override
    public void onSuccess(Object object) {

    }

    @Override
    public void onFailed(String message) {

    }
}
