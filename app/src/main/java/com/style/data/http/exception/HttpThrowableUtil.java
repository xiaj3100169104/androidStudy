package com.style.data.http.exception;

import android.content.Context;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import javax.net.ssl.HttpsURLConnection;

import retrofit2.HttpException;

/**
 * Created by xiajun on 2018/7/23.
 */

public class HttpThrowableUtil {

    public static void handleHttpError(Context context, Throwable e) {
        e.printStackTrace();
        //网络断开
        if (e instanceof UnknownHostException) {
            showToast(context, "网络连接断开");
        } else if (e instanceof SocketTimeoutException) {
            showToast(context, "网络连接超时");
        } else if (e instanceof ConnectException) {
            showToast(context, "服务君跑去火星啦");
        } else if (e instanceof HttpException) {
            HttpException he = (HttpException) e;
            switch (he.code()) {
                case HttpsURLConnection.HTTP_UNAUTHORIZED:
                    onTokenError(context);
                    break;
                case HttpsURLConnection.HTTP_INTERNAL_ERROR:
                    showToast(context, "服务器繁忙");
                    break;
                case HttpsURLConnection.HTTP_NOT_FOUND:
                    showToast(context, "服务君跑去火星啦");
                    break;
            }
        } else if (e instanceof ResultErrorException) {
            //请求失败
            showToast(context, ((ResultErrorException) e).msg);
        }
    }

    protected static void onTokenError(Context context) {
        showToast(context, "授权过期，请稍候重试");
        //getMvpView().onError(R.string.str_unauthorized);
        //updateAccessToken();
    }

    private static void showToast(Context context, String s) {

    }

}
