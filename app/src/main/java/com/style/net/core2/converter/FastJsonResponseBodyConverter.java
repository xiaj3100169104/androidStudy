package com.style.net.core2.converter;

import com.alibaba.fastjson.JSON;
import com.style.net.core2.response.BaseRes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by xiajun on 2018/4/26.
 */

public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Type type;

    public FastJsonResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource bufferedSource = Okio.buffer(value.source());
        String tempStr = bufferedSource.readUtf8();
        BaseRes baseRes = JSON.parseObject(tempStr, BaseRes.class);
        if (baseRes != null && !baseRes.isSuccess()) {
            bufferedSource.close();
            throw new ResultErrorException(baseRes.ischeck, baseRes.message);
        }
        try {
            return JSON.parseObject(baseRes.data, type);
        } finally {
            bufferedSource.close();
        }
    }
}