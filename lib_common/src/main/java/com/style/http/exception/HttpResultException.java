package com.style.http.exception;



public class HttpResultException extends RuntimeException {

    public static final int NETWORK_ERROR = 9998; //网络异常
    public static final int SERVER_ERROR = 500; //服务器异常
    public static final int TOKEN_INVALID = 1000; //TOKEN无效
    public static final int TOKEN_EXPIRED = 1001; //TOKEN过期
    public int errorCode;
    public String msg;

    public HttpResultException(int code, String msg1) {
        this.errorCode = code;
        this.msg = msg1;
    }

    public static HttpResultException serverError() {
        return new HttpResultException(SERVER_ERROR, "服务器异常");
    }
}
