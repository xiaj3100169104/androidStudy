package example.subThreadLooper;

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import com.style.base.BaseTitleBarActivity
import com.style.framework.databinding.ActivityMsgToSubBinding

public class MsgToSubActivity : BaseTitleBarActivity() {

    private lateinit var bd: ActivityMsgToSubBinding
    lateinit var mHandler: Handler
    private lateinit var subThread: MyHandlerThread

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        bd = ActivityMsgToSubBinding.inflate(layoutInflater)
        setContentView(bd.root)
        initThreadHandler()
        bd.tvSend.setOnClickListener { send() }
    }

    private fun initThreadHandler() {
        subThread = MyHandlerThread()
        subThread.start()
        //创建一个handler和当前线程绑定；handleMessage也是在当前线程中执行
        mHandler = @SuppressLint("HandlerLeak")
        object : Handler(subThread.getLooper()!!) {
            override fun handleMessage(msg: Message) {
                runOnUiThread { bd.tvContent.setText("主线程发来新消息啦") }
            }
        }
    }

    private fun send() {
        mHandler.sendEmptyMessage(1)
    }

    override fun onDestroy() {
        super.onDestroy()
        mHandler.removeCallbacksAndMessages(null)
    }
}
