package example.queue;

import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.style.framework.R;

public class QueueTestActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_test);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button2:
                testInstance();
                break;

            case R.id.button3:
                testQueue();
                break;
        }
    }

    private void testQueue() {
        for (int i = 0; i < 500; i++) {
            final int k = i;
            ThreadPoolUtil.execute(new Runnable() {
                @Override
                public void run() {
                    TestManager.getInstance().test(k);

                }
            });
        }
    }

    private void testInstance() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500; i++) {
                    TestManager.getInstance().test(i);
                }
            }
        }).start();

    }
}
