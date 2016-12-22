package com.style.constant;


import android.os.Environment;

public class FileDirectory {
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
