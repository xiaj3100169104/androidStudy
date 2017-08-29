package com.style.rxAndroid.callback;

import android.util.Log;

import java.util.concurrent.Callable;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by xiajun on 2016/10/8.
 */
public abstract class BaseRXTaskCallBack {
    protected String TAG = "BaseRXTaskCallBack";

    public Disposable run() {
        //被观察者
        Observable<Object> mObservable = Observable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() {
                Log.e(TAG, "call");
                return doInBackground();
            }
        });
        //定义生产事件线程
        mObservable.subscribeOn(Schedulers.io());
      /*  Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> e) throws Exception {
                Log.d("所在的线程：",Thread.currentThread().getName());
                Log.d("发送的数据:", 1+"");
                e.onNext(1);
            }
        });*/
        //定义消费事件线程
        mObservable.observeOn(AndroidSchedulers.mainThread());
        //消费者
        Consumer<Object> observer = new Consumer<Object>() {
            @Override
            public void accept(Object o) throws Exception {
                Log.e(TAG, "accept");
                onNextOnUIThread(o);
            }
        };
        //一次性用品，可以连接或断开Observer(观察者)与Observable(被观察者)
        Disposable disposable = mObservable.subscribe(observer);
        return disposable;
    }

    protected abstract void onNextOnUIThread(Object o);

    public abstract Object doInBackground();
}
