package com.style.data.http.function.impl;

import com.style.data.http.function.WebNetSource;
import com.style.data.http.function.WebNetSourcekt;
import com.style.http.core.RetrofitImpl;
import com.style.http.core.RxSchedulers;

import io.reactivex.Observable;

public final class WebNetSourceImpl {

    private static final WebNetSource mAPI;
    public static final WebNetSourcekt mAPI2;

    static {
        mAPI = RetrofitImpl.getInstance().getDefaultRetrofit().create(WebNetSource.class);
        mAPI2 = RetrofitImpl.getInstance().getDefaultRetrofit().create(WebNetSourcekt.class);
    }

    public static Observable<String> getPhoneInfo(String phone) {
        return mAPI.getMobileLocation(phone, "").compose(RxSchedulers.applySchedulers());
    }

    public static Observable<String> getWeather(String cityCode) {
        return mAPI.getWeatherInfo(cityCode, "").compose(RxSchedulers.applySchedulers());
    }

    public static Observable<String> getKuaiDi(String type, String postid) {
        return mAPI.getKuaiDi("yuantong", "11111111111").compose(RxSchedulers.applySchedulers());
    }
}
