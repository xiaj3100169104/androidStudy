package com.style.newwork.common;


import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

/**
 * Created by xiajun on 2016/12/22.
 */
public class NetWorkManager {
    protected String TAG = getClass().getSimpleName();

    private static NetWorkManager instance;

    public synchronized static NetWorkManager getInstance() {
        if (instance == null) {
            instance = new NetWorkManager();
        }
        return instance;
    }

    public void post(RequestParams params, Callback callback) {

        OkHttpUtils.post().url(params.url)
                .params(params.getParams())
                //.addParams("password", "123")
                .build()
                .execute(callback);
    }


}

