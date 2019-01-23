package com.style.data.http.converter;

import java.io.IOException;

import okhttp3.ResponseBody;
import okio.Okio;
import retrofit2.Converter;

/**
 * 原始字符串转换器
 */

public class OriginalStringConverter implements Converter<ResponseBody, String> {

    @Override
    public String convert(ResponseBody value) throws IOException {
        return Okio.buffer(value.source()).readUtf8();
    }
}