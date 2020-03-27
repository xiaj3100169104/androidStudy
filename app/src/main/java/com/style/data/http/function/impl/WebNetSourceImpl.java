package com.style.data.http.function.impl;

import com.style.data.http.function.WebNetSource;
import com.style.http.core.RetrofitImpl;
import com.style.http.core.RxSchedulers;

import io.reactivex.Observable;

public final class WebNetSourceImpl {
    private static WebNetSource mAPI;

    static {
        mAPI = RetrofitImpl.getInstance().getDefaultRetrofit().create(WebNetSource.class);
    }

    public static Observable<String> getPhoneInfo(String phone) {
        return mAPI.getMolileLocation(phone, "").compose(RxSchedulers.applySchedulers());
    }

    public static Observable<String> getWeather(String cityCode) {
        return mAPI.getWeatherInfo(cityCode, "").compose(RxSchedulers.applySchedulers());
    }

    public static Observable<String> getKuaiDi(String userName, String password) {
        return mAPI.getKuaiDi("yuantong", "11111111111").compose(RxSchedulers.applySchedulers());
    }
}
