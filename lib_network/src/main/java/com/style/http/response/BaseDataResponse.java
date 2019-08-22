package com.style.http.response;

public class BaseDataResponse<T> {
    public static final int SUCCESS = 0;

    public int code;

    public T data;

    public String msg;

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMsg() {
        return this.msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isOk() {
        return code == SUCCESS;
    }
}
