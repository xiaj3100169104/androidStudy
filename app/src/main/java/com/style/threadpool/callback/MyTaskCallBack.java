package com.style.threadpool.callback;

import android.util.Log;


/**
 * Created by xiajun on 2016/10/8.
 */
public  class MyTaskCallBack extends CustomFutureTask {
    protected String TAG = "MyTaskCallBack";

    public Object doInBackground(){
        Log.e(TAG, "doInBackground");
        return null;
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onSuccess(Object object) {

    }

    @Override
    public void onFailed(String message) {

    }
}
