package com.style.net.core2;

import example.newwork.response.LoginBean;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by xiajun on 2017/12/21.
 */

public interface APIFunction {
    @POST("/app/changePsd.html")
    Observable<BaseResult<LoginBean>> login(@Field("userName") String name, @Field("password") String password);

    @POST("http://www.kuaidi100.com/query?") @FormUrlEncoded
    Observable<BaseResult<KuaiDiModel>> getKuaiDi(@Field("type") String name, @Field("postid") String password);

    @POST("http://www.kuaidi100.com/query?") @FormUrlEncoded
    Observable<String> getKuaiDi2(@Field("type") String name, @Field("postid") String password);
}
