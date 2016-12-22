package test.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import org.greenrobot.eventbus.EventBus;

import test.event.MessageEvent;

public class SecondActivity extends BaseToolBarActivity {

    private EditText mMessageET;

    @Override
    public void initData() {
        mMessageET = (EditText) findViewById(R.id.messageET);
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("postEvent", Thread.currentThread().getName());
                        String message = mMessageET.getText().toString();
                        if(TextUtils.isEmpty(message)) {
                            message = "defaule message";
                        }
                        EventBus.getDefault().post(new MessageEvent("1"));

                    }
                }).start();
               /* Log.e("postEvent", Thread.currentThread().getName());
                String message = mMessageET.getText().toString();
                if(TextUtils.isEmpty(message)) {
                    message = "defaule message";
                }
                EventBus.getDefault().post(new MessageEvent(message));*/
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_second;
        super.onCreate(savedInstanceState);

    }

}
