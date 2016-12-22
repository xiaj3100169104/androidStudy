package test.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import test.event.MessageEvent;

public class StickyModeActivity extends BaseToolBarActivity {

    int index = 0;

    @Override
    public void initData() {
        findViewById(R.id.post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new MessageEvent("test" + index++));
            }
        });
        findViewById(R.id.regist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().register(StickyModeActivity.this);
            }
        });

        findViewById(R.id.unregist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().unregister(StickyModeActivity.this);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_sticky_mode;
        super.onCreate(savedInstanceState);
    }

    @Subscribe(threadMode = ThreadMode.POSTING, sticky = true)
    public void onMessageEventPostThread(MessageEvent messageEvent) {
        Log.e("PostThread", messageEvent.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onMessageEventMainThread(MessageEvent messageEvent) {
        Log.e("MainThread", messageEvent.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    public void onMessageEventBackgroundThread(MessageEvent messageEvent) {
        Log.e("BackgroundThread", messageEvent.getMessage());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC, sticky = true)
    public void onMessageEventAsync(MessageEvent messageEvent) {
        Log.e("Async", messageEvent.getMessage());
    }

}
