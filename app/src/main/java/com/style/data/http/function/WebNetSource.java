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
    Observable<String> getMolileLocation(@Field("mobileCode") String mobileCode, @Field("userID") String userID);

    @POST("http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather")
    @FormUrlEncoded
    Observable<String> getWeatherInfo(@Field("theCityCode") String mobileCode, @Field("theUserID") String userID);

    @POST("http://www.kuaidi100.com/query?")
    @FormUrlEncoded
    Observable<String> getKuaiDi(@Field("type") String name, @Field("postid") String password);
}
