package com.style.data.http.exception;



public class ResultErrorException extends RuntimeException {

    public static final String REQUEST_FAILED = "9998"; //请求失败
    public String errorCode;
    public String msg;


    public ResultErrorException(String code) {
        this.errorCode = code;
        this.msg = getMsg(errorCode);
    }

    public ResultErrorException(String code, String msg1) {
        this.errorCode = code;
        this.msg = msg1;
    }

    public String getMsg(String errorCode) {
        String msg = "未知错误";
        switch (errorCode) {
            case REQUEST_FAILED:
                msg = "请求失败";
                break;
        }
        return msg;
    }
}
