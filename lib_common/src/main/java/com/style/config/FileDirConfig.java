package com.style.config;


import android.os.Environment;

public class FileDirConfig {
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

    public static final String FILE_PROVIDER_AUTHORITY = "com.style.file.provider";
}
