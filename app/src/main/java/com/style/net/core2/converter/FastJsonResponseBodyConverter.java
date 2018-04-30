package com.style.net.core2.converter;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.style.net.bean.UserInfo;
import com.style.net.core2.response.BaseHttpResponse;
import com.style.net.core2.response.TokenResponse;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import okio.BufferedSource;
import okio.Okio;
import retrofit2.Converter;

/**
 * Created by xiajun on 2018/4/26.
 */

public class FastJsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private Type type;

    public FastJsonResponseBodyConverter(Type type) {
        this.type = type;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        BufferedSource bufferedSource = Okio.buffer(value.source());
        String tempStr = bufferedSource.readUtf8();
        bufferedSource.close();
        T data;
        if (type == String.class) {
            if (TextUtils.isEmpty(tempStr)) {
                throw new ResultErrorException(ResultErrorException.REQUEST_FAILED);
            }
            return (T) tempStr;
        }
        if (type == TokenResponse.class) {
            data = JSON.parseObject(tempStr, type);
            if (data == null) {
                throw new ResultErrorException(ResultErrorException.REQUEST_FAILED);
            }
            return data;
        }
        BaseHttpResponse baseRes = JSON.parseObject(tempStr, BaseHttpResponse.class);
        if (baseRes == null) {
            throw new ResultErrorException(ResultErrorException.REQUEST_FAILED);
        } else {
            //如果只需要解析成功时，不需要关心或者解析数据
            if (type == BaseHttpResponse.class && baseRes.isSuccess()) {
                return (T) baseRes;
            }
            if (!baseRes.isSuccess()) {
                throw new ResultErrorException(ResultErrorException.REQUEST_FAILED);
            }
        }

        data = JSON.parseObject(baseRes.getData(), type);
        //数据为空时，防止rxjava接受null
        if (data == null)
            try {
                Class<T> cls = (Class<T>) type;
                return cls.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                throw new ResultErrorException(ResultErrorException.REQUEST_FAILED);
            }
        return data;
    }
}