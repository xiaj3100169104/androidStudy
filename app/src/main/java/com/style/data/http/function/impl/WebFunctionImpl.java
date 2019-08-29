package com.style.data.http.function.impl;

import com.style.data.http.function.WebFunction;

import io.reactivex.Observable;

//@SuppressWarnings("unchecked")
public final class WebFunctionImpl extends BaseFunctionImpl {


    public static Observable<String> getPhoneInfo(String phone) {
        return getDefaultService(WebFunction.class).getMolileLocation(phone, "").compose(applySchedulers());
    }

    public static Observable<String> getWeather(String cityCode) {
        return getDefaultService(WebFunction.class).getWeatherInfo(cityCode, "").compose(applySchedulers());
    }

    public static Observable<String> getKuaiDi(String userName, String password) {
        return getDefaultService(WebFunction.class).getKuaiDi("yuantong", "11111111111").compose(applySchedulers());
    }
}
