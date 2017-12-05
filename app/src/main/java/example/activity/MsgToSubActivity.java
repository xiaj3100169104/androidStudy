package example.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MsgToSubActivity extends BaseToolBarActivity {


    @Bind(R.id.tv_send)
    TextView tvSend;
    @Bind(R.id.tv_content)
    TextView tvContent;

    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_msg_to_sub;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                //创建一个handler和当前线程绑定；handleMessage也是在当前线程中执行
                mHandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        MsgToSubActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvContent.setText("主线程发来新消息那");
                            }
                        });
                    }
                };
                Looper.loop();
            }
        }).start();
    }

    @OnClick(R.id.tv_send)
    public void send() {
        mHandler.sendEmptyMessage(1);

    }
}
