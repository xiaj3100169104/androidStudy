package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.style.base.BaseToolBarActivity;
import com.style.constant.ConfigUtil;
import com.style.framework.R;
import com.style.framework.databinding.ActivityFileDownBinding;
import com.style.net.file.FileCallback;
import com.style.net.file.MultiThreadDownloadManager;
import com.style.view.progressbar.HorizontalProgressBar;

import java.io.File;

public class FileDownActivity extends BaseToolBarActivity {

    ActivityFileDownBinding bd;

    private String url = "http://archive.apache.org/dist/tomcat/tomcat-8/v8.0.24/bin/apache-tomcat-8.0.24.exe";
    private String url2 = "http://sw.bos.baidu.com/sw-search-sp/software/13d93a08a2990/ChromeStandalone_55.0.2883.87_Setup.exe";
    private String url3 = "http://wdl1.cache.wps.cn/wps/download/W.P.S.50.391.exe";

    private String targetPath = ConfigUtil.DIR_APP_FILE + "/apache-tomcat-8.0.24_multi_thread.exe";

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_file_down);
        initData();
    }

    @Override
    public void initData() {
        super.customTitleOptions(bd.getRoot());

        setToolbarTitle("文件下载");

        bd.btHttpDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down();
            }
        });
        bd.btMultiThreadDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down2();
            }
        });
        bd.btMultiFileDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down3();
            }
        });
    }

    private void down() {

    }


    private void down2() {
        MultiThreadDownloadManager.getInstance().down(TAG, url, targetPath, new FileCallback() {
            @Override
            public void start(int fileSize) {
                logE(TAG, "下载开始，文件大小==" + fileSize);
            }

            @Override
            public void inProgress(int currentDownSize, int fileSize, float progress) {
                bd.progressBar4.setProgress((int) (100 * progress));
            }

            @Override
            public void complete(String filePath) {
                logE(TAG, "下载完成，文件路径==" + filePath);
            }
        });
    }


    private void down3() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MultiThreadDownloadManager.getInstance().cancelCallback(TAG);
    }
}
