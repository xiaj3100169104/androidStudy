package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityMsgToSubBinding;


public class MsgToSubActivity extends BaseToolBarActivity {
    ActivityMsgToSubBinding bd;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_msg_to_sub);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                //创建一个handler和当前线程绑定；handleMessage也是在当前线程中执行
                mHandler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        MsgToSubActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                bd.tvContent.setText("主线程发来新消息那");
                            }
                        });
                    }
                };
                Looper.loop();
            }
        }).start();
    }

    public void send(View v) {
        mHandler.sendEmptyMessage(1);

    }
}
