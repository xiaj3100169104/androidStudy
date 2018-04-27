package com.style.net.core2;

import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by xiajun on 2017/12/22.
 */

public class InterceptorUtil {
    public static String TAG = "InterceptorUtil";

    //日志拦截器
    public static HttpLoggingInterceptor LogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.e(TAG, message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);//设置打印数据的级别
    }

    public static Interceptor HeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();
                Request.Builder newBuilder = original.newBuilder();
                //newBuilder.addHeader("Content-Type", "application/json");
                //newBuilder.addHeader("Authorization", "Bearer " + AccountManager.getInstance().getAccessToken());
                Request newRequest = newBuilder.build();
                return chain.proceed(newRequest);
            }
        };

    }
}
