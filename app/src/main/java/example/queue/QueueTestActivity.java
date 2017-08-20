package example.queue;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.style.framework.R;

public class QueueTestActivity extends AppCompatActivity implements View.OnClickListener, EventReceiver {

    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_queue_test);
        textView = (TextView) findViewById(R.id.tv_content);
        findViewById(R.id.button2).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        EventManager.getInstance().register(this, 1);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            EventManager.getInstance().post(1, k);
        }

    }

    private void testInstance() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 500; i++) {
                    //EventManager.getInstance().test(i);
                }
            }
        }).start();

    }

    @Override
    public void onMainThreadEvent(int code, Object data) {
        if (code == 1) {
            // Integer g = (Integer) data;
            Log.e("data==", data + "");
            textView.setText(data + "");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventManager.getInstance().unRegister(this);
    }
}
