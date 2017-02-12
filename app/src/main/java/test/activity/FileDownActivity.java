package test.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.style.base.BaseToolBarActivity;
import com.style.constant.ConfigUtil;
import com.style.framework.R;
import com.style.newwork.common.NetWorkManager;
import com.style.newwork.moultithreaddown.MultiThreadDownloadTask;
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

    private String url = "http://archive.apache.org/dist/tomcat/tomcat-8/v8.0.24/bin/apache-tomcat-8.0.24.exe";
    private String url2 = "http://www.igniterealtime.org/downloadServlet?filename=openfire/openfire_4_0_4.exe";
    private String url3 = "http://www.igniterealtime.org/downloadServlet?filename=spark/spark_2_8_2.exe";

    private String targetPath = ConfigUtil.DIR_APP_FILE + "/apache-tomcat-8.0.24.exe";

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
        NetWorkManager.getInstance().down(url, new FileCallBack(ConfigUtil.DIR_APP_FILE, "http. exe") {
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
        MultiThreadDownloadTask task = new MultiThreadDownloadTask(url, 5, targetPath);
        task.start();
    }


    private void down3() {
        NetWorkManager.getInstance().down(url2, new FileCallBack(ConfigUtil.DIR_APP_FILE, "file1. exe") {
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
    }
}
