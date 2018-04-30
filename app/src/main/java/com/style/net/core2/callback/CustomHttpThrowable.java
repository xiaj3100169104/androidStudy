package com.style.net.core2.callback;

import android.support.annotation.StringRes;

import com.style.manager.AppManager;
import com.style.manager.ToastManager;
import com.style.net.core2.converter.ResultErrorException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

import io.reactivex.functions.Consumer;
import retrofit2.HttpException;

/**
 * Created by xiajun on 2018/4/29.
 */

public class CustomHttpThrowable implements Consumer<Throwable> {

    @Override
    public void accept(Throwable e) throws Exception {
        e.printStackTrace();
        //网络断开
        if (e instanceof UnknownHostException) {
            onErrorMsg("网络连接断开");
        } else if (e instanceof SocketTimeoutException) {
            onErrorMsg("网络连接超时");
        } else if (e instanceof HttpException) {
            HttpException he = (HttpException) e;
            switch (he.code()) {
                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                    onErrorMsg("授权过期，请稍候重试");
                    break;
                case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                    onErrorMsg("服务器繁忙");
                    break;
                case HttpsURLConnection.HTTP_NOT_FOUND:
                    onErrorMsg("跑去火星啦");
                    break;
            }
        } else if (e instanceof ResultErrorException) {
            //请求失败
            onErrorMsg(((ResultErrorException) e).msg);
        }
    }

    public void onError() {
    }

    private void onErrorMsg(@StringRes int resId) {
        onErrorMsg(AppManager.getInstance().getContext().getString(resId));
    }

    private void onErrorMsg(String msg) {
        ToastManager.showToast(AppManager.getInstance().getContext(), msg);
    }
}
