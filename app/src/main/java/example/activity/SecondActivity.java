package example.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import org.simple.eventbus.EventBus;

public class SecondActivity extends BaseToolBarActivity {

    private EditText mMessageET;

    @Override
    public void initData() {
        mMessageET = (EditText) findViewById(R.id.messageET);
        findViewById(R.id.send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }
}
