package com.style.data.fileDown.entity;

import com.style.data.fileDown.FileDownloadStateBean;

import java.io.Serializable;

/**
 * 根据sd卡文件数据下载了多少更新界面下载进度
 */
public class CustomFileBean implements Serializable {
    public String url;
    public String title;
    public String fileName;
    public int totalSize = 0;
    public FileDownloadStateBean fileStatus;

    public CustomFileBean(String url, String title, String fileName) {
        this.url = url;
        this.title = title;
        this.fileName = fileName;
    }
}