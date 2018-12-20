package example.activity;

import android.os.Bundle
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityMsgToSubBinding;


public class MsgToSubActivity : BaseDefaultTitleBarActivity() {
    lateinit var bd: ActivityMsgToSubBinding;

    lateinit var mHandler: Handler;

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.activity_msg_to_sub)
        bd = getBinding();
        Thread(object : Runnable {
            override fun run() {
                Looper.prepare();
                //创建一个handler和当前线程绑定；handleMessage也是在当前线程中执行
                mHandler = object : Handler() {
                    override fun handleMessage(msg: Message?) {
                        runOnUiThread(object : Runnable {
                            override fun run() {
                                bd.tvContent.setText("主线程发来新消息那");
                            }
                        });
                    }
                };
                Looper.loop();
            }
        }).start();
    }

    fun send(v: View) {
        mHandler.sendEmptyMessage(1);
    }
}
