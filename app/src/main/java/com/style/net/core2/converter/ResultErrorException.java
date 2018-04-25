package com.style.net.core2.converter;

/**
 * Created by xiajun on 2018/4/25.
 */

public class ResultErrorException extends RuntimeException {

    public final String ischeck;
    public final String message;

    public ResultErrorException(String ischeck, String message) {
        this.ischeck = ischeck;
        this.message = message;
    }
}
