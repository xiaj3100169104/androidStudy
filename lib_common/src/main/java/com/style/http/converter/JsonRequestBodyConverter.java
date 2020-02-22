package com.style.http.converter;

import com.alibaba.fastjson.JSON;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

/**
 * json格式请求转换器
 */

public class JsonRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("application/json; charset=UTF-8");

    @Override
    public RequestBody convert(T value) {
        return RequestBody.create(MEDIA_TYPE, JSON.toJSONBytes(value));
    }
}
