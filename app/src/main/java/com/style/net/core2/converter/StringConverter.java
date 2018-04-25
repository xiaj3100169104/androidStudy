package com.style.net.core2.converter;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Converter;

/**
 * Created by xiajun on 2017/12/21.
 */

public class StringConverter implements Converter<ResponseBody, String> {

    public static final StringConverter INSTANCE = new StringConverter();

    @Override
    public String convert(ResponseBody value) throws IOException {
        return value.string();
    }
}
