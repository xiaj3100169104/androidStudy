package com.style.app;


import android.os.Environment;

import com.style.framework.BuildConfig;

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
    public static final String DIR_APP = Environment.getExternalStorageDirectory() + "/aaaaStyle";
    /**
     * 图片保存目录
     */
    public static final String DIR_APP_IMAGE_CAMERA = DIR_APP + "/image";
    /**
     * 文件保存目录
     */
    public static final String DIR_APP_FILE = DIR_APP + "/file";
    /**
     * 文件缓存目录
     */
    public static final String DIR_CACHE = DIR_APP + "/cache";
    /**
     * 视频录制保存目录
     */
    public static final String DIR_VIDEO = DIR_APP + "/video";

    public static final String FILE_PROVIDER_AUTHORITY = BuildConfig.APPLICATION_ID + ".file.provider";

}
