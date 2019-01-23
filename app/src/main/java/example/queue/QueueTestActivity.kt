package example.queue

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import com.style.data.singlePriorityTask.PrioritizedTask
import com.style.data.singlePriorityTask.SinglePriorityTaskManager

import com.style.framework.R
import com.style.utils.BytesHexStrTranslate
import kotlinx.android.synthetic.main.activity_queue_test.*
import java.util.*

class QueueTestActivity : AppCompatActivity(), EventReceiver {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_queue_test)
        btn_register.setOnClickListener {
            EventManager.getInstance().register(this, 1)
        }
        btn_unregister.setOnClickListener {
            EventManager.getInstance().unRegister(this)
        }
        btn_send_event.setOnClickListener {
            EventManager.getInstance().post(1, tv_content.text.toString())
        }
        val random = Random()
        btn_priority_queue.setOnClickListener {
            for (i in 0..10) {
                val t = PrioritizedTask(i.toString(), random.nextInt(50))
                SinglePriorityTaskManager.getInstance().addTask(t.id, t)
            }
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
