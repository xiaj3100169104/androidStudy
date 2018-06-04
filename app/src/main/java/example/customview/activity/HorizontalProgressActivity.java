package example.customview.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.style.framework.R;
import com.style.view.progressbar.HorizontalProgressBar;


public class HorizontalProgressActivity extends AppCompatActivity {

    private HorizontalProgressBar progressBar;
    private HorizontalProgressBar progressBar2;
    private HorizontalProgressBar progressBar3;
    private HorizontalProgressBar progressBar4;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int progress = msg.getData().getInt("progress");
            switch (msg.what) {
                case 1:
                    progressBar.setProgress(progress);
                    break;
                case 2:
                    progressBar2.setProgress(progress);
                    break;
                case 3:
                    progressBar3.setProgress(progress);
                    break;
                case 4:
                    progressBar4.setProgress(progress);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_progress);
        progressBar = (HorizontalProgressBar) findViewById(R.id.progress);
        progressBar2 = (HorizontalProgressBar) findViewById(R.id.progress2);
        progressBar3 = (HorizontalProgressBar) findViewById(R.id.progress3);
        progressBar4 = (HorizontalProgressBar) findViewById(R.id.progress4);

        progressBar.setProgressColor(Color.BLACK);
        progressBar.setProgress(100);
        findViewById(R.id.bt_download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                down();
            }
        });

    }

    protected void down() {
        DownloadThread downloadThread = new DownloadThread(1, 100);
        DownloadThread downloadThread2 = new DownloadThread(2, 50);
        DownloadThread downloadThread3 = new DownloadThread(3, 20);
        DownloadThread downloadThread4 = new DownloadThread(4, 10);
        downloadThread.start();
        downloadThread2.start();
        downloadThread3.start();
        downloadThread4.start();
    }

    private class DownloadThread extends Thread {
        private int url;
        private long sleepTime;

        public DownloadThread(int url, long sleepTime) {
            this.url = url;
            this.sleepTime = sleepTime;
        }

        @Override
        public void run() {
            super.run();
            for (int i = 0; i <= 100; i++) {
                Message msg = handler.obtainMessage(url);
                Bundle b = new Bundle();
                b.putInt("progress", i);
                msg.setData(b);
                handler.sendMessage(msg);
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
