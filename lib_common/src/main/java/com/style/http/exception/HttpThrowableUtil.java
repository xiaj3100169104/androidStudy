package com.style.http.exception;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import retrofit2.HttpException;

/**
 * Created by xiajun on 2018/7/23.
 */

public class HttpThrowableUtil {

    public static HttpResultException handleHttpError(Throwable e) {
        e.printStackTrace();
        HttpResultException ex;
        //网络断开
        if (e instanceof UnknownHostException) {
            ex = onNetworkError("网络连接断开");
        } else if (e instanceof SocketTimeoutException) {
            ex = onNetworkError("网络连接超时");
        } else if (e instanceof ConnectException) {
            ex = onNetworkError("服务君跑去火星啦");
        } else if (e instanceof HttpException) {
            ex = onNetworkError("服务君跑去火星啦");
        } else if (e instanceof HttpResultException) {
            ex = (HttpResultException) e;
        } else {
            ex = new HttpResultException(HttpResultException.SERVER_ERROR, "服务君跑去火星啦");
        }
        return ex;
    }

    private static HttpResultException onNetworkError(String s) {
        return new HttpResultException(HttpResultException.NETWORK_ERROR, s);
    }

}
