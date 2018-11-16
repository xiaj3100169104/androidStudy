package com.style.data.net.core;

import android.text.TextUtils;
import android.util.Log;

import com.style.data.prefs.AppPrefsManager;
import com.style.framework.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class InterceptorUtil {
    public static String TAG = "InterceptorUtil";

    //日志拦截器
    public static HttpLoggingInterceptor LogInterceptor() {
        return new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                if (BuildConfig.LOG_ENABLE)
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
                if (TextUtils.isEmpty(original.header("Authorization")))//这里没打印Authorization因为执行在日志拦截后
                    newBuilder.addHeader("Authorization", AppPrefsManager.Companion.getInstance().getSignKey());
                Request newRequest = newBuilder.build();
                return chain.proceed(newRequest);
            }
        };

    }
}
