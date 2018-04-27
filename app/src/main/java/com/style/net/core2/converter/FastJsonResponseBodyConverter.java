package com.style.net.core2.converter;

import com.alibaba.fastjson.JSON;
import com.style.net.core2.response.BaseHttpResponse;
import com.style.net.core2.response.TokenResponse;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

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
        if (type == String.class) {
            bufferedSource.close();
            return (T) tempStr;
        }
        if (type == TokenResponse.class) {
            bufferedSource.close();
            return JSON.parseObject(tempStr, type);
        }
        BaseHttpResponse baseRes = JSON.parseObject(tempStr, BaseHttpResponse.class);
        if (baseRes == null) {
            throw new ResultErrorException(ResultErrorException.REQUEST_FAILED);
        } else {
            //如果只需要解析成功时，不需要关心或者解析数据
            if (type == BaseHttpResponse.class && baseRes.isSuccess()) {
                bufferedSource.close();
                return (T) baseRes;
            }
            if (!baseRes.isSuccess()) {
                bufferedSource.close();
                throw new ResultErrorException(ResultErrorException.REQUEST_FAILED);
            }
        }
        //资源关闭
        bufferedSource.close();
        return JSON.parseObject(baseRes.getData(), type);
    }
}