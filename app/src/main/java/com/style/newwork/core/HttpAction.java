package com.style.newwork.core;

/**
 * Created by vong on 2016/5/19.
 */
public class HttpAction {
    protected String TAG = "HttpAction";
    private static String testUrl = "https://www.hao123.com/";

    public static final String REAL_SERVER = "http://www.wangzongwen.cn";//"http://192.168.199.108:8080";
    public static final String REAL_API_URL = REAL_SERVER + "/wechat_server";
    public static final String LOGIN = REAL_API_URL + "/login";

    public static void login(String name) {

        OkHttpUtil.postAsyn(LOGIN, name);
    }

}

