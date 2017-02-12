package com.style.newwork.moultithreaddown;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * 多线程下载文件
 * Created by xiajun on 2017/2/12.
 */
public class MultiThreadDownloadTask extends Thread {
    private static final String TAG = MultiThreadDownloadTask.class.getSimpleName();

    private String downloadUrl;// 下载链接地址
    private int threadNum;// 开启的线程数
    private String filePath;// 保存文件路径地址
    private int blockSize;// 每一个线程的下载量

    public MultiThreadDownloadTask(String downloadUrl, int threadNum, String filePath) {
        this.downloadUrl = downloadUrl;
        this.threadNum = threadNum;
        this.filePath = filePath;
    }

    @Override
    public void run() {

        FileDownloadThread[] threads = new FileDownloadThread[threadNum];
        try {
            URL url = new URL(downloadUrl);
            Log.e(TAG, "download file http path:" + downloadUrl);
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Charset", "UTF-8");
            // 读取下载文件总大小
            int fileSize = conn.getContentLength();
            if (fileSize <= 0) {
                System.out.println("读取文件失败");
                return;
            }
            // 设置ProgressBar最大的长度为文件Size
            //mProgressbar.setMax(fileSize);

            // 计算每条线程下载的数据长度
            blockSize = (fileSize % threadNum) == 0 ? fileSize / threadNum : fileSize / threadNum + 1;
            Log.d(TAG, "fileSize:" + fileSize + "  blockSize:" + blockSize);

            File file = new File(filePath);
            for (int i = 0; i < threads.length; i++) {
                // 启动线程，分别下载每个线程需要下载的部分
                int startPos = blockSize * (i);//开始位置
                int endPos = blockSize * (i + 1) - 1;//结束位置
                threads[i] = new FileDownloadThread(url, file, startPos, endPos);
                threads[i].setName("Thread:" + i);
                threads[i].start();
            }

            boolean isFinished = false;
            // 当前所有线程下载总量
            int downloadedAllSize = 0;
            while (!isFinished) {
                isFinished = true;
                downloadedAllSize = 0;
                for (int i = 0; i < threads.length; i++) {
                    downloadedAllSize += threads[i].getDownloadLength();
                    if (!threads[i].isCompleted()) {
                        isFinished = false;
                    }
                }
                // 通知handler去更新视图组件
               /* Message msg = new Message();
                msg.getData().putInt("size", downloadedAllSize);
                mHandler.sendMessage(msg);*/
                Log.e(TAG, "current downloadSize:" + downloadedAllSize);
                //Thread.sleep(1000);// 休息1秒后再读取下载进度
            }
            Log.d(TAG, " all of downloadSize:" + downloadedAllSize);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } /* catch (InterruptedException e) {
            e.printStackTrace();
        }*/

    }
}