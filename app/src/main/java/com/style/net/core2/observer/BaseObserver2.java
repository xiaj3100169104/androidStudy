package com.style.net.core2.observer;

import com.style.net.core2.RetrofitManager;
import com.style.net.core2.response.BaseResponse;
import com.style.net.core2.response.BaseResult;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Created by xiajun on 2017/12/21.
 */

public abstract class BaseObserver2<T> implements Observer<BaseResponse<T>> {

    //订阅上了回调
    @Override
    public void onSubscribe(@NonNull Disposable d) {

    }

    @Override
    public void onNext(@NonNull BaseResponse<T> tBaseResult) {
        if (tBaseResult.isSuccess()) {
            onSuccess(tBaseResult.data);
        }else {
            onFailed("请求失败");
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        onFailed(e.getMessage());
    }

    @Override
    public void onComplete() {

    }


    public void onSuccess(T data) {

    }

    public void onFailed(String message) {

    }
}
