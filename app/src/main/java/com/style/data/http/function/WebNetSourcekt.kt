package com.style.data.http.function

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * Created by xiajun on 2017/12/21.
 */
interface WebNetSourcekt {

    @POST("http://ws.webxml.com.cn/WebServices/WeatherWS.asmx/getWeather")
    @FormUrlEncoded
    suspend fun getWeatherInfo2(@Field("theCityCode") cityCode: String?, @Field("theUserID") userID: String?): String?

    @POST("http://www.kuaidi100.com/query?")
    @FormUrlEncoded
    suspend fun getKuaiDi2(@Field("type") type: String?, @Field("postid") postid: String?): String?
}