package example.queue

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log

import com.style.framework.R
import com.style.utils.BytesHexStrTranslate
import kotlinx.android.synthetic.main.activity_queue_test.*

class QueueTestActivity : AppCompatActivity(), EventReceiver {

    private var b22: ByteArray? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue_test)
        btn_hexString2Bytes.setOnClickListener {
            b22 = BytesHexStrTranslate.hexString2Bytes("01")
        }
        btn_bytes2HexString.setOnClickListener {
            var s11 = BytesHexStrTranslate.bytes2HexString(b22)
            tv_result.text = s11
        }
        btn_register.setOnClickListener {
            EventManager.getInstance().register(this, 1)
        }
        btn_unregister.setOnClickListener {
            EventManager.getInstance().unRegister(this)
        }
        btn_send_event.setOnClickListener {
            EventManager.getInstance().post(1, tv_content.text.toString())
        }
    }

    override fun onMainThreadEvent(code: Int, data: Any) {
        if (code == 1) {
            val s = data as String
            Log.e("data==", s + "")
            tv_result.text = s
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventManager.getInstance().unRegister(this)
    }
}
