package com.style.data.http.function;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by xiajun on 2017/12/21.
 */

public interface WebNetSource {

    @POST("http://ws.webxml.com.cn/WebServices/MobileCodeWS.asmx/getMobileCodeInfo")
    @FormUrlEncoded
    Observable<String> getMobileLocation(@Field("mobileCode") String mobileCode, @Field("userID") String userID);

    @POST("http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather")
    @FormUrlEncoded
    Observable<String> getWeatherInfo(@Field("theCityCode") String cityCode, @Field("theUserID") String userID);

    @POST("http://www.kuaidi100.com/query?")
    @FormUrlEncoded
    Observable<String> getKuaiDi(@Field("type") String type, @Field("postid") String postid);

    @POST("http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather")
    @FormUrlEncoded
    String getWeatherInfo2(@Field("theCityCode") String cityCode, @Field("theUserID") String userID);

    @POST("http://www.kuaidi100.com/query?")
    @FormUrlEncoded
    String getKuaiDi2(@Field("type") String type, @Field("postid") String postid);
}
