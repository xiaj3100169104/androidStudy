package com.style.data.net.core2.converter;



public class ResultErrorException extends RuntimeException {

    public static final int REQUEST_FAILED = 9998; //请求失败
    public static final int PARSE_FAILED = 9997; //数据解析失败
    public int errorCode;
    public String msg;


    public ResultErrorException(int code) {
        this.errorCode = code;
        this.msg = getMsg(errorCode);
    }

    public ResultErrorException(int code, String msg1) {
        this.errorCode = code;
        this.msg = msg1;
    }

    public String getMsg(int errorCode) {
        String msg = "未知错误";
        switch (errorCode) {
            case REQUEST_FAILED:
                msg = "请求失败";
                break;
        }
        return msg;
    }
}
