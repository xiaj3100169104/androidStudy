package com.style.data.http.converter;

import com.alibaba.fastjson.JSON;
import com.style.data.http.exception.ResultErrorException;
import com.style.data.http.response.BaseHttpResponse;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * 不友好的返回格式转换器
 */

public class BadResponseConverter<T> implements Converter<ResponseBody, T> {
    private Type type;

    public BadResponseConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource bufferedSource = Okio.buffer(value.source());
        String tempStr = bufferedSource.readUtf8();
        bufferedSource.close();
        BaseHttpResponse baseRes = JSON.parseObject(tempStr, BaseHttpResponse.class);
        if (baseRes == null) {
            throw new ResultErrorException(ResultErrorException.REQUEST_FAILED);
        } else if (!baseRes.isSuccess()) {
            throw new ResultErrorException(baseRes.getErrorCode(), baseRes.ErrorDescriptions);
        }
        T data = JSON.parseObject(tempStr, type);
        if (data == null) {
            try {
                Class<T> cls = (Class<T>) type;
                return cls.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new ResultErrorException(ResultErrorException.REQUEST_FAILED);
            }
        }
        return data;
    }
}