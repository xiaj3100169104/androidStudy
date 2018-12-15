package example.filedown;

import android.os.Bundle;
import android.view.View;

import com.style.app.FileDirConfig;
import com.style.base.BaseDefaultTitleBarActivity;
import com.style.data.net.file.FileCallback;
import com.style.data.net.file.MultiThreadDownloadManager;
import com.style.framework.R;
import com.style.framework.databinding.ActivityFileDownBinding;

import org.jetbrains.annotations.Nullable;

public class FileDownActivity extends BaseDefaultTitleBarActivity {

    ActivityFileDownBinding bd;

    private String url = "http://archive.apache.org/dist/tomcat/tomcat-8/v8.0.24/bin/apache-tomcat-8.0.24.exe";
    private String url2 = "http://sw.bos.baidu.com/sw-search-sp/software/13d93a08a2990/ChromeStandalone_55.0.2883.87_Setup.exe";
    private String url3 = "http://wdl1.cache.wps.cn/wps/download/W.P.S.50.391.exe";

    private String targetPath = FileDirConfig.DIR_APP_FILE + "/apache-tomcat-8.0.24_multi_thread.exe";

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_file_down);
    }

    @Override
    public void initData() {
        bd = getBinding();

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
        MultiThreadDownloadManager.getInstance().down(getTAG(), url, targetPath, new FileCallback() {
            @Override
            public void start(int fileSize) {
                logE(getTAG(), "下载开始，文件大小==" + fileSize);
            }

            @Override
            public void inProgress(int currentDownSize, int fileSize, float progress) {
                bd.progressBar4.setProgress((int) (100 * progress));
            }

            @Override
            public void complete(String filePath) {
                logE(getTAG(), "下载完成，文件路径==" + filePath);
            }
        });
    }


    private void down3() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MultiThreadDownloadManager.getInstance().cancelCallback(getTAG());
    }
}
