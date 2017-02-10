package test.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewConfigurationCompat;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.style.base.BaseToolBarActivity;
import com.style.constant.ConfigUtil;
import com.style.framework.R;
import com.style.utils.FileUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class FileDownActivity extends BaseToolBarActivity {

    @Bind(R.id.bt_option)
    Button btOption;
    private String path = "http://sw.bos.baidu.com/sw-search-sp/software/13d93a08a2990/ChromeStandalone_55.0.2883.87_Setup.exe";
    private MaterialProgressBar progressBar;

    @Override
    protected void onCreate(Bundle arg0) {
        mLayoutResID = R.layout.activity_file_down;
        super.onCreate(arg0);
    }

    @Override
    public void initData() {
        setToolbarTitle("文件下载");

        progressBar = (MaterialProgressBar) findViewById(R.id.MaterialProgressBar);
        progressBar.setMax(100);
        progressBar.setIndeterminate(false);

        btOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        option(5, path, ConfigUtil.DIR_APP_FILE, "测试文件.exe");

                    }
                }).start();
            }
        });
    }


    private void option(int threadCount, String remotePath, String dir, String fileName) {

        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("accept", "*/*");
            int code = conn.getResponseCode();
            if (code == 200) {
                //服务器返回的数据的长度，实际上就是文件的长度
                int length = conn.getContentLength();
                System.out.println("文件总长度:" + length);

                //在客户端本地
                //假设3个线程去下载资源
                //平均每一个线程下载的文件的大小。
                int blockSize = length / threadCount;

                for (int threadId = 1; threadId <= threadCount; ++threadId) {
                    //第一个线程下载的开始位置
                    int startIndex = (threadId - 1) * blockSize;
                    int endIndex = blockSize - 1;
                    if (threadId == threadCount) {
                        //最后一个线程下载的长度稍微长一点
                        endIndex = length;
                    }
                    System.out.println("线程:" + threadId + "下载:--" + startIndex + "-->" + endIndex);
                    new DownLoadThread(threadId, startIndex, endIndex, remotePath, dir, fileName).start();
                }
            } else {
                System.out.println("访问错误");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载文件的子线程，每个线程下载对应的文件
     */
    public static class DownLoadThread extends Thread {
        private int threadId;
        private int startIndex;
        private int endIndex;
        private String remotePath;
        private String dir;
        private String fileName;

        public DownLoadThread(int threadId, int startIndex, int endIndex, String remotePath, String dir, String fileName) {
            this.threadId = threadId;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.remotePath = remotePath;
            this.dir = dir;
            this.fileName = dir;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(remotePath);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                //很重要：请求服务器下载部分的文件的指定的位置：
                conn.setRequestProperty("Range", "bytes=" + startIndex + "-" + endIndex);
                conn.setConnectTimeout(5000);
                conn.setRequestProperty("connection", "keep-alive");
                conn.setRequestProperty("accept", "*/*");
                int code = conn.getResponseCode();//从服务器请求全部资源 200ok ,如果请求部分资源 206 ok
                System.out.println("code=" + code);

                InputStream is = conn.getInputStream();//返回资源
                File file = FileUtil.create(dir, fileName);
                RandomAccessFile raf = new RandomAccessFile(file, "rwd");
                //随机写文件的时候从哪个位置开始写
                raf.seek(startIndex);//定位文件

                int len = 0;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    raf.write(buffer, 0, len);
                }
                is.close();
                raf.close();
                System.out.println("线程" + threadId + "下载完毕");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
