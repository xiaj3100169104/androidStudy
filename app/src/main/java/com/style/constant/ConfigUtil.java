package com.style.constant;


import android.os.Environment;

public class ConfigUtil {

    /**
     * app服务器路径
     */
    public static final String REAL_SERVER = "http://www.wangzongwen.cn";
    //public static final String REAL_SERVER = "http://192.168.255.204:8080";
    public static final String REAL_API_URL = REAL_SERVER + "/wechat_server";

    /**
     * app文件根目录
     */
    public static final String DIR_APP = Environment.getExternalStorageDirectory() + "/style";
    /**
     * 图片保存目录
     */
    public static final String DIR_APP_IMAGE_CAMERA = DIR_APP + "/image";
    /**
     * 文件缓存目录
     */
    public static final String DIR_APP_IMAGE_CACHE = DIR_APP + "/cache";

}
