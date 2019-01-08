package com.style.data.net.file;

import android.util.Log;

import com.style.data.event.EventBusEvent;

import org.simple.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.style.data.net.file.FileDownloadStateBean.Companion.DownStatus;

/**
 * 单线程下载文件
 * 中断两种情况:
 * 一种是当线程处于阻塞状态或者试图执行一个阻塞操作时，我们可以使用实例方法interrupt()进行线程中断，
 * 执行中断操作后将会抛出interruptException异常(该异常必须捕捉无法向外抛出)并将中断状态复位;
 * 另外一种是当线程处于运行状态时，我们也可调用实例方法interrupt()进行线程中断，
 * 但同时必须手动判断中断状态，并编写中断线程的代码(其实就是结束run方法体的代码)。
 */
public class SingleFileDownloadTask implements Runnable {
    private static final String TAG = SingleFileDownloadTask.class.getSimpleName();

    private String url;// 下载链接地址
    private String filePath;// 保存文件路径地址
    private int fileLength;//文件总大小
    private int downloadLength;//已下载了多少（包括之前下载过的）
    private File file;//目标文件
    private int startPos;//起始下载位置

    public SingleFileDownloadTask(String url, int startPos, String filePath) {
        this.url = url;
        this.startPos = startPos;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        file = new File(filePath);
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        BufferedInputStream bis = null;
        RandomAccessFile raf = null;
        try {
            URL u = new URL(url);
            Log.e(TAG, "file url:" + u);
            HttpURLConnection conn = (HttpURLConnection) u.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//默认浏览器编码类型
            conn.setRequestProperty("Range", "bytes=" + startPos + "-");
            //conn.addRequestProperty("Connection","Keep-Alive");//设置与服务器保持连接
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_PARTIAL) {
                // 读取下载文件总大小(注意区分是不是从头开始下载)
                fileLength = conn.getContentLength() + startPos;
                Log.e(TAG, "fileLength:" + fileLength);
                downloadLength = startPos;
                onFileDownloading();
                //保存数据库
                byte[] buffer = new byte[1024];
                bis = new BufferedInputStream(conn.getInputStream());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(startPos);
                //每读取这么多字节数据时才发送一次更新进度条事件
                int perSendLength = fileLength / 100;
                int perReadCount = 0;
                int len;
                while ((len = bis.read(buffer, 0, 1024)) != -1) {
                    raf.write(buffer, 0, len);
                    downloadLength += len;
                    //最好不要让消息发送太频繁，根据进度条计算发送次数与时机
                    perReadCount += len;
                    if (perReadCount >= perSendLength) {
                        Log.e(TAG, "downloadLength:" + downloadLength);
                        onFileDownloading();
                        perReadCount = 0;
                    }
                }
                onFileDownloaded();
            }
        } catch (IOException e) {
            e.printStackTrace();
            onFileDownloadInterrupted();
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

    /**
     * 下载中断
     */
    private void onFileDownloadInterrupted() {
        FileDownloadStateBean b = new FileDownloadStateBean(url);
        b.setStatus(DownStatus.DOWNLOAD_PAUSE);
        b.setTotalSize(this.fileLength);
        b.setDownloadSize(this.downloadLength);
        EventBus.getDefault().post(b, EventBusEvent.FILE_DOWNLOAD_STATE_CHANGED);
    }

    /**
     * 下载完成
     */
    private void onFileDownloaded() {
        FileDownloadStateBean b = new FileDownloadStateBean(url);
        b.setStatus(DownStatus.DOWNLOAD_COMPLETED);
        b.setTotalSize(this.fileLength);
        b.setDownloadSize(this.downloadLength);
        EventBus.getDefault().post(b, EventBusEvent.FILE_DOWNLOAD_STATE_CHANGED);
    }

    /**
     * 下载中
     */
    private void onFileDownloading() {
        FileDownloadStateBean b = new FileDownloadStateBean(url);
        b.setStatus(DownStatus.DOWNLOADING);
        b.setTotalSize(this.fileLength);
        b.setDownloadSize(this.downloadLength);
        EventBus.getDefault().post(b, EventBusEvent.FILE_DOWNLOAD_STATE_CHANGED);
    }
}