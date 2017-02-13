package test.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.style.base.BaseToolBarActivity;
import com.style.constant.ConfigUtil;
import com.style.framework.R;
import com.style.newwork.common.NetWorkManager;
import com.style.newwork.filedown.FileDownCallback;
import com.style.newwork.filedown.MultiThreadDownManager;
import com.style.newwork.filedown.MultiThreadDownloadTask;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import butterknife.Bind;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;
import okhttp3.Call;

public class FileDownActivity extends BaseToolBarActivity {

    @Bind(R.id.bt_http_down)
    Button btHttpDown;
    @Bind(R.id.bt_multi_thread_down)
    Button btMultiThreadDown;
    @Bind(R.id.bt_multi_file_down)
    Button btMultiFileDown;
    @Bind(R.id.progressBar)
    MaterialProgressBar progressBar;
    @Bind(R.id.progressBar2)
    MaterialProgressBar progressBar2;
    @Bind(R.id.progressBar3)
    MaterialProgressBar progressBar3;
    @Bind(R.id.progressBar4)
    MaterialProgressBar progressBar4;
    private String url = "http://archive.apache.org/dist/tomcat/tomcat-8/v8.0.24/bin/apache-tomcat-8.0.24.exe";
    private String url2 = "http://sw.bos.baidu.com/sw-search-sp/software/13d93a08a2990/ChromeStandalone_55.0.2883.87_Setup.exe";
    private String url3 = "http://wdl1.cache.wps.cn/wps/download/W.P.S.50.391.exe";

    private String targetPath = ConfigUtil.DIR_APP_FILE + "/apache-tomcat-8.0.24_multi_thread.exe";

    @Override
    protected void onCreate(Bundle arg0) {
        mLayoutResID = R.layout.activity_file_down;
        super.onCreate(arg0);
    }

    @Override
    public void initData() {
        setToolbarTitle("文件下载");

        progressBar.setMax(100);
        progressBar.setIndeterminate(false);
        progressBar2.setMax(100);
        progressBar2.setIndeterminate(false);
        progressBar3.setMax(100);
        progressBar3.setIndeterminate(false);
        progressBar4.setMax(100);
        progressBar4.setIndeterminate(false);
        btHttpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down();
            }
        });
        btMultiThreadDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down2();
            }
        });
        btMultiFileDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down3();
            }
        });
    }

    private void down() {
        NetWorkManager.getInstance().down(url, new FileCallBack(ConfigUtil.DIR_APP_FILE, "apache-tomcat-8.0.24_http.exe") {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError :" + e.getMessage());
            }

            @Override
            public void onResponse(File file, int id) {
                Log.e(TAG, "onResponse :" + file.getAbsolutePath());
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                progressBar.setProgress((int) (100 * progress));
            }
        });
    }


    private void down2() {
        MultiThreadDownManager.getInstance().down(TAG, url, targetPath, new FileDownCallback() {
            @Override
            public void start(int fileSize) {
                logE(TAG, "下载开始，文件大小==" + fileSize);
            }

            @Override
            public void inProgress(int currentDownSize, int fileSize, float progress) {
                progressBar4.setProgress((int) (100 * progress));
            }

            @Override
            public void complete(String filePath) {
                logE(TAG, "下载完成，文件路径==" + filePath);
            }
        });
    }


    private void down3() {
        NetWorkManager.getInstance().down(url2, new FileCallBack(ConfigUtil.DIR_APP_FILE, "file1.exe") {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError :" + e.getMessage());
            }

            @Override
            public void onResponse(File file, int id) {
                Log.e(TAG, "onResponse :" + file.getAbsolutePath());
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                progressBar2.setProgress((int) (100 * progress));
            }
        });
        NetWorkManager.getInstance().down(url3, new FileCallBack(ConfigUtil.DIR_APP_FILE, "file2.exe") {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.e(TAG, "onError :" + e.getMessage());
            }

            @Override
            public void onResponse(File file, int id) {
                Log.e(TAG, "onResponse :" + file.getAbsolutePath());
            }

            @Override
            public void inProgress(float progress, long total, int id) {
                progressBar3.setProgress((int) (100 * progress));
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        NetWorkManager.getInstance().cancel(url);
        MultiThreadDownManager.getInstance().cancelCallback(TAG);
    }
}
