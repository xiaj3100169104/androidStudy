package com.style.net.core;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONObject;
import com.style.constant.ConfigUtil;
import com.style.manager.AccountManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by xiajun on 2016/12/22.
 */
public class HttpActionManager {
    protected String TAG = "HttpManager";
    private static String URL_BASE_REMOTE = "http://ws.webxml.com.cn/WebServices/";
    private HttpActionService service;

    private Map<String, List<Call>> mTaskMap = new HashMap();
    private static HttpActionManager mInstance;
    public static HttpActionManager getInstance() {
        if (mInstance == null) {
            mInstance = new HttpActionManager();
        }
        return mInstance;
    }

    public void runTask(String tag, Call call, Callback callback) {
        call.enqueue(callback);
        addTask(tag, call);
    }

    public void addTask(String tag, Call call) {
        List<Call> callList = mTaskMap.get(tag);
        if (callList == null) {
            callList = new ArrayList<>();
            mTaskMap.put(tag, callList);
        }
        callList.add(call);
    }

    public void removeTask(String tag) {
        List<Call> callList = mTaskMap.get(tag);
        if (callList != null) {
            for (Call s : callList) {
                if (s != null && !s.isCanceled()) {
                    s.cancel();
                }
            }
        }
    }
    public void init() {
        Retrofit retrofit = new Retrofit.Builder()
                .client(getClient())
                .addConverterFactory(ScalarsConverterFactory.create())
                .baseUrl(URL_BASE_REMOTE)
                .build();
        service = retrofit.create(HttpActionService.class);
        //initLocalService();
    }

    private OkHttpClient getClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                //添加请求拦截
                //.connectTimeout(5, TimeUnit.SECONDS)//设置超时时间
                .retryOnConnectionFailure(true);
               /* .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request original = chain.request();

                        // Request customization: add request headers
                        Request.Builder newBuilder = original.newBuilder();
                        String token = AccountManager.getInstance().getToken();
                        if (!TextUtils.isEmpty(token)) {
                            newBuilder.header("token", AccountManager.getInstance().getToken());
                        }
                        Request request = newBuilder.build();
                        return chain.proceed(request);
                    }
                });*/

        return client.build();
    }
    public void getPhoneInfo(String tag, String phone, Callback callback) {

        Call<String> call = service.login(phone, "");
        runTask(tag, call, callback);
    }
    public void getWeather(String tag, String code, Callback callback) {

        Call<String> call = service.getWeather(code, "");
        runTask(tag, call, callback);
    }
}

