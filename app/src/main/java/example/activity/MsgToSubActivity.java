package example.activity;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityMsgToSubBinding;


public class MsgToSubActivity extends BaseActivity {
    ActivityMsgToSubBinding bd;

    Handler mHandler;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_msg_to_sub;
    }

    @Override
    public void initData() {
        bd = getBinding();
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
