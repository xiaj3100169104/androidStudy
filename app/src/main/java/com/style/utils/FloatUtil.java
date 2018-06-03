package com.style.utils;

/**
 * Created by xiajun on 2018/5/9.
 */

public class FloatUtil {

    public static int getSmallInt(float value) {
        return (int) Math.floor(value);
    }

    public static int getBigInt(float value) {
        return (int) Math.ceil(value);
    }
}
