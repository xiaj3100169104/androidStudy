package com.style.net.core;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by xiajun on 2017/3/25.
 */


public interface HttpActionService {

    @POST("MobileCodeWS.asmx/getMobileCodeInfo")
    @FormUrlEncoded
    Call<String> login(@Field("mobileCode") String mobileCode, @Field("userID") String userID);

    @POST("WeatherWS.asmx/getWeather")
    @FormUrlEncoded
    Call<String> getWeather(@Field("theCityCode") String mobileCode, @Field("theUserID") String userID);

    @POST("/app/changePsd.html")
    Call<String> alertPassword(@Body RequestBody requestBody);

    @GET("/app/taskPlan/list.html")
    Call<String> getHomeTaskList();

    @GET("/app/taskProgress/listRepeat.html")
    Call<String> getReCommitList();

    @POST("/app/taskProgress/add.html")
    Call<String> commit2server(@Body RequestBody requestBody);

    @POST("/app/taskProgress/repeatSubmit.html")
    Call<String> repeatCommit(@Body RequestBody requestBody);

}
