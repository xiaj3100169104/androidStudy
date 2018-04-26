package com.style.net.core2.converter;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.style.net.core2.response.BaseRes;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Created by xiajun on 2018/4/25.
 */

public class BaseResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;

    BaseResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string();
        BaseRes baseRes = JSON.parseObject(response, BaseRes.class);
        if (baseRes != null && !baseRes.isSuccess()) {
            value.close();
            throw new ResultErrorException(baseRes.ischeck, baseRes.message);
        }
        try {
            MediaType contentType = value.contentType();
            Charset charset = contentType != null ? contentType.charset(UTF_8) : UTF_8;
            InputStream inputStream = new ByteArrayInputStream(baseRes.data.getBytes());
            Reader reader = new InputStreamReader(inputStream, charset);
            JsonReader jsonReader = gson.newJsonReader(reader);
            return adapter.read(jsonReader);
        } finally {
            value.close();
        }


    }
}
