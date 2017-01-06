package com.style.newwork.common;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;


public class NetBeanCallback<T> extends NetStringCallback {
    protected String TAG = getClass().getSimpleName();
    protected Class<T> clazz;
    protected TypeReference<T> type;

    public NetBeanCallback() {

    }

    public NetBeanCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    public NetBeanCallback(TypeReference<T> type) {
        this.type = type;
    }

    public void onResponse(String result) {
        T data = null;
        if (this.clazz != null)
            data = JSON.parseObject(result, this.clazz);
        if (this.type != null)
            data = JSON.parseObject(result, this.type);

        onResultSuccess(data);
    }


    protected void onResultSuccess(T data) {

    }

    protected void onFailure(String msg) {

    }

}
