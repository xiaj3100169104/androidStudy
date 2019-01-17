package com.style.data.net.core;

import com.style.entity.KuaiDi;
import com.style.entity.UserInfo;
import com.style.data.net.request.LoginRequest;
import com.style.data.net.response.BaseResult;
import com.style.data.net.response.TokenResponse;

import java.util.List;

import example.newwork.response.LoginBean;
import io.reactivex.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by xiajun on 2017/12/21.
 */

public interface APIFunction {

    @POST("http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx/getMobileCodeInfo")
    @FormUrlEncoded
    Observable<String> getMolileLocation(@Field("mobileCode") String mobileCode, @Field("userID") String userID);

    @POST("http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather")
    @FormUrlEncoded
    Observable<String> getWeatherInfo(@Field("theCityCode") String mobileCode, @Field("theUserID") String userID);

    @POST("/app/changePsd.html")
    Observable<BaseResult<LoginBean>> login(@Field("userName") String name, @Field("password") String password);

    @POST("http://www.kuaidi100.com/query?")
    @FormUrlEncoded
    Observable<String> getKuaiDi(@Field("type") String name, @Field("postid") String password);

    @POST("guardian/pupillus")
    Observable<List<KuaiDi>> getPupil(@Body RequestBody requestBody);

    @Headers("Authorization:Basic YW5kcm9pZGNsaWVudDo4QTcyOUZENC04NjdGLTREMTItOEE4Ri1CQTNFOEQ4MzhERjM=")
    @POST("https://watch.lemonnc.com/OAuth/Token")
    @FormUrlEncoded
    Observable<TokenResponse> getToken(@Field("grant_type") String grant_type);

    @POST("guardian/login")
    Observable<UserInfo> login2(@Body LoginRequest requestBody);

    @POST("http://40.73.116.12:8009/LMService/Health/Medical/TCM_Callback")
    Observable<ResponseBody> test();
}
