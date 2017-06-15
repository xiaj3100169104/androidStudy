package example.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import example.event.MessageEvent;

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
        //注册事件
        EventBus.getDefault().register(this);
    }

    //在产生事件的线程中执行
    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onMessageEventPostThread(MessageEvent messageEvent) {
        Log.e("PostThread", Thread.currentThread().getName());
    }

    //在UI线程中执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread(MessageEvent messageEvent) {
        Log.e("MainThread", Thread.currentThread().getName());
        Log.e("messageEvent", messageEvent.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEventMainThread2(MessageEvent messageEvent) {
        Log.e("MainThread", Thread.currentThread().getName());
        Log.e("messageEvent", messageEvent.getMessage());
    }

    //如果产生事件的是UI线程，则在新的线程中执行。如果产生事件的是非UI线程，则在产生事件的线程中执行
    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onMessageEventBackgroundThread(MessageEvent messageEvent) {
        Log.e("BackgroundThread", Thread.currentThread().getName());
    }

    //无论产生事件的是否是UI线程，都在新的线程中执行
    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onMessageEventAsync(MessageEvent messageEvent) {
        Log.e("Async", Thread.currentThread().getName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }
}
