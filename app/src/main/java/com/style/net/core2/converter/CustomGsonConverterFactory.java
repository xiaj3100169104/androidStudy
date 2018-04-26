package com.style.net.core2.converter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by xiajun on 2018/4/25.
 */

public class CustomGsonConverterFactory extends Converter.Factory {

    private Gson gson;

    public CustomGsonConverterFactory create() {
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd hh:mm:ss").create();
        return new CustomGsonConverterFactory();

    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new BaseResponseBodyConverter<>(gson, adapter);
    }
}
