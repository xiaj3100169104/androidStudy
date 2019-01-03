package com.style.data.net.file;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 单线程下载文件
 * Created by xiajun on 2017/2/12.
 */
public class SingleFileDownloadThread extends Thread {
    private static final String TAG = SingleFileDownloadThread.class.getSimpleName();

    private static final int DOWN_START = 2;
    private static final int DOWN_PROGRESS = 3;
    private static final int DOWN_COMPLETE = 4;

    private Object tag;//界面标志
    private FileCallback fileDownCallback;
    private boolean canCallback = true;//是否需要执行回调,默认true

    private String downloadUrl;// 下载链接地址
    private String filePath;// 保存文件路径地址
    private int fileLength;//文件总大小
    private int downloadLength;
    private File file;//目标文件

    public SingleFileDownloadThread(Object tag, String downloadUrl, String filePath, FileCallback fileDownCallback) {
        this.tag = tag;
        this.downloadUrl = downloadUrl;
        this.filePath = filePath;
        this.fileDownCallback = fileDownCallback;
    }

    @Override
    public void run() {
        file = new File(filePath);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        BufferedInputStream bis = null;
        RandomAccessFile raf = null;
        try {
            URL url = new URL(downloadUrl);
            Log.e(TAG, "file url:" + downloadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//默认浏览器编码类型
            //conn.addRequestProperty("Connection","Keep-Alive");//设置与服务器保持连接
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_OK) {
                // 读取下载文件总大小
                fileLength = conn.getContentLength();
                Log.e(TAG, "fileLength:" + fileLength);
                byte[] buffer = new byte[1024];
                bis = new BufferedInputStream(conn.getInputStream());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(startPos);
                int len;
                while ((len = bis.read(buffer, 0, 1024)) != -1) {
                    raf.write(buffer, 0, len);
                    downloadLength += len;
                    Log.e(TAG, "downloadLength:" + downloadLength);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (raf != null) {
                try {
                    raf.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setCanCallback(boolean canCallback) {
        this.canCallback = canCallback;
    }

    public boolean isCallbackEnable() {
        if (this.canCallback && this.fileDownCallback != null)
            return true;
        return false;
    }

    public void setCallback(FileCallback callback) {
        this.fileDownCallback = callback;
    }
}