package example.activity;

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityMsgToSubBinding;
import kotlinx.android.synthetic.main.activity_msg_to_sub.*


public class MsgToSubActivity : BaseDefaultTitleBarActivity() {

    lateinit var mHandler: Handler
    private lateinit var subThread: Thread

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.activity_msg_to_sub)
        initThreadHandler()
        tv_send.setOnClickListener { send() }
    }

    private fun initThreadHandler() {
        subThread = Thread(Runnable {
            Looper.prepare()
            //创建一个handler和当前线程绑定；handleMessage也是在当前线程中执行
            mHandler = @SuppressLint("HandlerLeak")
            object : Handler() {
                override fun handleMessage(msg: Message?) {
                    runOnUiThread { tv_content.setText("主线程发来新消息啦") }
                }
            }
            Looper.loop()
        })
        subThread.start()
    }

    private fun send() {
        mHandler.sendEmptyMessage(1)
    }
}
