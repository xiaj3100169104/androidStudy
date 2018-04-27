package com.style.net.core2.response;

import android.text.TextUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by Administrator on 2017/5/15.
 */

public class BaseHttpResponse {


    /**
     * Data : null
     * ErrorCode : 20002
     * Message : login failure
     * ErrorDescriptions : [{"Language":"en","Info":"Login failed,The user name or password error!"},{"Language":"zh","Info":"登录失败,用户名或密码错误！"}]
     * Success : false
     */

    private String Data;
    private String ErrorCode;
    private String Message;
    private boolean Success;
    private List<ErrorDescriptionsBean> ErrorDescriptions;

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

    public List<ErrorDescriptionsBean> getErrorDescriptions() {
        return ErrorDescriptions;
    }

    public void setErrorDescriptions(List<ErrorDescriptionsBean> ErrorDescriptions) {
        this.ErrorDescriptions = ErrorDescriptions;
    }

    public static class ErrorDescriptionsBean {
        /**
         * Language : en
         * Info : Login failed,The user name or password error!
         */

        private String Language;
        private String Info;

        public String getLanguage() {
            return Language;
        }

        public void setLanguage(String Language) {
            this.Language = Language;
        }

        public String getInfo() {
            return Info;
        }

        public void setInfo(String Info) {
            this.Info = Info;
        }

        @Override
        public String toString() {
            return "ErrorDescriptionsBean{" +
                    "Language='" + Language + '\'' +
                    ", Info='" + Info + '\'' +
                    '}';
        }
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

    /**
     * 根据语言环境解析错误描述
     *
     * @return
     */
    public String getErrorDescription() {
        String des = null;
        String language = Locale.getDefault().getLanguage();
        if (ErrorDescriptions != null && !ErrorDescriptions.isEmpty()) {
            for (ErrorDescriptionsBean bean : ErrorDescriptions) {
                if (bean.getLanguage().equals(language)) {
                    des = bean.getInfo();
                    break;
                }
            }
            if (TextUtils.isEmpty(des)) {
                des = ErrorDescriptions.get(0).getInfo();
            }
        }
        return des;
    }
}
