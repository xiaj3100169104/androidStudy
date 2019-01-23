package com.style.data.http.response;

import android.text.TextUtils;

import java.util.List;
import java.util.Locale;

public class BaseHttpResponse {

    public String Data;
    public String ErrorCode;
    public String Message;
    public boolean Success;
    //错误描述：可能有多种语言应该在请求时确定需要返回哪种语言
    public String ErrorDescriptions;

    public String getData() {
        return Data;
    }

    public void setData(String Data) {
        this.Data = Data;
    }

    public String getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(String ErrorCode) {
        this.ErrorCode = ErrorCode;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public boolean isSuccess() {
        return Success;
    }

    public void setSuccess(boolean Success) {
        this.Success = Success;
    }

    @Override
    public String toString() {
        return "BaseHttpResponse{" +
                "Data=" + Data +
                ", ErrorCode='" + ErrorCode + '\'' +
                ", Message='" + Message + '\'' +
                ", Success=" + Success +
                ", ErrorDescriptions=" + ErrorDescriptions +
                '}';
    }
}
