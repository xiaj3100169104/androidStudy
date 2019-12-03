package com.style.http.converter;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * ResponseBody转换器
 */

public class ResponseBodyConverter implements Converter<ResponseBody, ResponseBody> {

    @Override
    public ResponseBody convert(ResponseBody value) {
        return value;
    }
}
