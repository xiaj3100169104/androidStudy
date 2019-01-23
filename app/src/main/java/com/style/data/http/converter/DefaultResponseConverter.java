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
 * 一般返回格式转换器
 */

public class DefaultResponseConverter<T> implements Converter<ResponseBody, T> {
    private Type type;

    public DefaultResponseConverter(Type type) {
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
        //如果只需要解析成功失败时，不需要关心或者解析数据
        if (type == BaseHttpResponse.class) {
            return (T) baseRes;
        }
        //其他数据类型
        T data = JSON.parseObject(baseRes.getData(), type);
        //数据为空时，防止rxjava接受null
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