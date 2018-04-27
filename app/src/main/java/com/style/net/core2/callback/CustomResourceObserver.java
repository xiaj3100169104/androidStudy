package com.style.net.core2.callback;

import com.style.net.core2.converter.ResultErrorException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.observers.ResourceObserver;
import retrofit2.HttpException;

/**
 * Created by xiajun on 2018/4/27.
 */

public class CustomResourceObserver<T> extends ResourceObserver<T> {
    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        //网络断开
        if (e instanceof UnknownHostException) {

        } else if (e instanceof SocketTimeoutException) {

        } else if (e instanceof HttpException) {
            HttpException he = (HttpException) e;
            switch (he.code()) {
                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                    break;
                case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                    break;
                case HttpsURLConnection.HTTP_NOT_FOUND:
                    break;
            }
        } else if (e instanceof ResultErrorException) {
            //请求失败
        }
    }

    @Override
    public void onComplete() {

    }
}
