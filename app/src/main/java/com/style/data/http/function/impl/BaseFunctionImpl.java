package com.style.data.http.function.impl;

import com.style.http.core.RetrofitImpl;
import com.style.http.core.RxSchedulers;

import io.reactivex.ObservableTransformer;

public class BaseFunctionImpl {

    static <T> ObservableTransformer<T, T> applySchedulers() {
        return RxSchedulers.applySchedulers();
    }

    static <T> T getDefaultService(final Class<T> service) {
        return RetrofitImpl.getInstance().getDefaultRetrofit().create(service);
    }

}
