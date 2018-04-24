package com.style.net.core2.response;

public class BaseResponse<T> {
    public static final String SUCCESS = "0";

    public static final int SERVER_ERROR = 500; //服务器异常
    public static final int PARAM_MISS = 501; //传递参数有错误
    public static final int TOKEN_INVALID = 1000; //TOKEN过期
    public static final int TOKEN_EXPIRED = 1001; //TOKEN无效

    public String ischeck;

    public T data;

    public String message;

    public boolean isSuccess() {
        return SUCCESS.equals(ischeck);
    }
}
