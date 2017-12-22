package com.style.net.core2;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiajun on 2017/12/21.
 */

public class StringObserver implements Observer<String> {
    private String tag;

    public StringObserver(String tag) {
        this.tag = tag;
    }

    //订阅上了回调
    @Override
    public void onSubscribe(@NonNull Disposable d) {
        RetrofitManager.getInstance().addTask(tag, d);
    }

    @Override
    public void onNext(@NonNull String tBaseResult) {
        onSuccess(tBaseResult);

    }

    @Override
    public void onError(@NonNull Throwable e) {
        e.printStackTrace();
        onFailed(e.getMessage());
    }

    @Override
    public void onComplete() {

    }


    public void onSuccess(String data) {

    }

    public void onFailed(String message) {

    }
}
