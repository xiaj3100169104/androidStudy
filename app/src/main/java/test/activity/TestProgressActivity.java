package test.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.style.framework.R;
import com.style.view.progressbar.HorizontalProgressBar;


public class TestProgressActivity extends AppCompatActivity {

    private HorizontalProgressBar view2;

    private int k;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what ==1){
                view2.setProgress(k);

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horizontal_progress);
        view2 = (HorizontalProgressBar) findViewById(R.id.progress);
        view2.setProgressColor(Color.BLACK);
        view2.setProgress(100);

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i <= 100; i++) {
                    handler.sendEmptyMessage(1);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    k++;
                }
            }
        }).start();


    }
}
