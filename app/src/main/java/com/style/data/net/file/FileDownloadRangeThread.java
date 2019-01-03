package com.style.data.net.file;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * volatile这个关键字的意义起作用的仅仅是在编译的时候起作用，学过c语言应该知道，c语言中有一种寄存变量，
 * 而java中没有寄存器变量命名的关键字， 那么问题来了？ java中到底存不存在寄存类型变量呢？
 * 存在， java的编译器在编译时会自动将某些变量变为寄存变量。
 * 而volatile关键字的作用是，告诉编译器，不要将这个变量改变为寄存变量。
 * 寄存器是一个在cpu中的存储器， 访问寄存器中的数据比访问内存中的数据快得多。
 * <p>
 * Range 还有几种不同的方式来限定范围，可以根据需要灵活定制：
 * 1. 500-1000：指定开始和结束的范围，一般用于多线程下载。
 * 2. 500- ：指定开始区间，一直传递到结束。这个就比较适用于断点续传、或者在线播放等等。
 * 3. -500：无开始区间，只意思是需要最后 500 bytes 的内容实体。
 * 4. 100-300,1000-3000：指定多个范围，这种方式使用的场景很少，了解一下就好了
 * <p>
 * 资源变化:
 * 有时下载的过程中资源可以能已经发生变化了这时就需要重新下载， 可以通过 ETag 或者 Last-Modified 来标识当前资源是否变化
 * ETag：当前文件的一个验证令牌指纹，用于标识文件的唯一性。
 * Last-Modified：标记当前文件最后被修改的时间。
 * 只需要在头信息中传入 etag或 Last-Modified的值，若果资源没有发生变化会继续返回206，否则返回200，此时需要重新下载
 * <p>
 * If-Range： xxx
 * If-Range 必须与 Range 配套使用。如果没有 Range，那么 If-Range 就会被忽略。
 * 如果服务器不支持 If-Range，那么 Range 也会被忽略。
 * Etag（Entity Tags）主要为了解决 Last-Modified 无法解决的一些问题。
 * 1、某些文件周期性改变，但内容没变，只是时间变了
 * 2、某些文件改变非常频繁（1s变化n次），后者只可以精确到秒
 * 3、某些服务器不能精确得到文件的修改时间
 */
public class FileDownloadRangeThread extends Thread {

    private static final String TAG = FileDownloadRangeThread.class.getSimpleName();

    /**
     * 当前下载是否完成
     */
    private boolean isCompleted = false;
    /**
     * 当前下载文件长度
     */
    private int downloadLength = 0;
    /**
     * 文件保存路径
     */
    private File file;
    /**
     * 文件下载路径
     */
    private URL url;
    /**
     * 下载始位置
     */
    private int startPos;
    /**
     * 下载结束位置
     */
    private int endPos;

    public FileDownloadRangeThread(URL url, File file, int startPos, int endPos) {
        this.url = url;
        this.file = file;
        this.startPos = startPos;
        this.endPos = endPos;
    }

    @Override
    public void run() {
        //设置当前线程下载的起点、终点
        Log.e(TAG, Thread.currentThread().getName() + "  bytes=" + startPos + "-" + endPos);
        BufferedInputStream bis = null;
        RandomAccessFile raf = null;
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //如果为 true，则在允许用户交互（例如弹出一个验证对话框）的上下文中对此 URL 进行检查。
            conn.setAllowUserInteraction(true);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Range", "bytes=" + startPos + "-" + endPos);
            int code = conn.getResponseCode();
            if (code == HttpURLConnection.HTTP_PARTIAL) {
                byte[] buffer = new byte[1024];
                bis = new BufferedInputStream(conn.getInputStream());
                raf = new RandomAccessFile(file, "rwd");
                raf.seek(startPos);
                int len;
                while ((len = bis.read(buffer, 0, 1024)) != -1) {
                    raf.write(buffer, 0, len);
                    downloadLength += len;
                }
                isCompleted = true;
                Log.e(TAG, getName() + " is finished,all size:" + downloadLength);
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

    /**
     * 线程文件是否下载完毕
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * 线程下载文件长度
     */
    public int getDownloadLength() {
        return downloadLength;
    }

}