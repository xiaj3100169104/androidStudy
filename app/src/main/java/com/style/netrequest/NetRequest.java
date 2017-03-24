package com.style.netrequest;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by xiajun on 2017/3/24.
 */

public class NetRequest {

    private static NetRequest mInstance;
    private APIService service;

    public static NetRequest getInstance() {
        if (mInstance == null) {
            mInstance = new NetRequest();
        }
        return mInstance;
    }

    public void init(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.baidu.com/")
                //.addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(APIService.class);
    }

    public interface APIService {

        @POST("?tn=99671753_s_hao_pg")
        Call<String> test();

    }

    public void test() {

        Call<String> call = service.test();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.e("test", response.body());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }
}
