package com.style.data.http.function.impl;


import com.style.data.http.function.UserFunction;
import com.style.data.http.request.LoginRequest;
import com.style.data.http.response.LoginBean;
import com.style.data.http.response.TokenResponse;
import com.style.entity.KuaiDi;
import com.style.entity.UserInfo;
import com.style.http.response.BaseDataResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

//@SuppressWarnings("unchecked")
public final class UserFunctionImpl extends BaseFunctionImpl {

    public static Observable<BaseDataResponse<LoginBean>> login(String userName, String password) {
        Observable<BaseDataResponse<LoginBean>> mObservable = getDefaultService(UserFunction.class).login(userName, password);
        return mObservable.compose(applySchedulers());
    }

    public static Observable<List<KuaiDi>> getPupil(String guardianId) {
        JSONObject o = new JSONObject();
        try {
            o.put("GuardianId", guardianId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), o.toString());
        return getDefaultService(UserFunction.class).getPupil(requestBody);
    }

    public static Observable<BaseDataResponse<TokenResponse>> getToken() {
        return getDefaultService(UserFunction.class).getToken("client_credentials").compose(applySchedulers());
    }

    public static Observable<BaseDataResponse<UserInfo>> login2(String userName, String passWord) {
        return getDefaultService(UserFunction.class).login2(new LoginRequest(userName, passWord)).compose(applySchedulers());
    }

    public static Observable<ResponseBody> test() {
        return getDefaultService(UserFunction.class).test().compose(applySchedulers());
    }
}
