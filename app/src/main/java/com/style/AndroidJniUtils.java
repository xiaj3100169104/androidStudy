package com.style;

/**
 * Created by xiajun on 2017/5/27.
 */

public class AndroidJniUtils {
    static {
        System.loadLibrary("helloNDK");
    }
    public native String sayHello();
}
