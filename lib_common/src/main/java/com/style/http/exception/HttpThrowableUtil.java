package com.style.http.exception;

/**
 * Created by xiajun on 2018/7/23.
 */

public class HttpThrowableUtil {

    public static HttpResultException handleHttpError(Throwable e) {
        e.printStackTrace();
        HttpResultException ex;
        if (e instanceof HttpResultException) {
            ex = (HttpResultException) e;
        } else {
            ex = new HttpResultException(HttpResultException.NETWORK_ERROR, "网络异常");
        }
        return ex;
    }
}
