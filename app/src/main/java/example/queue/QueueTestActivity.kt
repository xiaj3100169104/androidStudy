package example.queue

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.style.data.singlePriorityTask.PrioritizedTask
import com.style.data.singlePriorityTask.SinglePriorityTaskManager
import com.style.framework.databinding.ActivityQueueTestBinding
import java.util.*

class QueueTestActivity : AppCompatActivity(), EventReceiver {

    private lateinit var bd: ActivityQueueTestBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bd = ActivityQueueTestBinding.inflate(layoutInflater)
        setContentView(bd.root)
        bd.btnRegister.setOnClickListener {
            EventManager.getInstance().register(this, 1)
        }
        bd.btnUnregister.setOnClickListener {
            EventManager.getInstance().unRegister(this)
        }
        bd.btnSendEvent.setOnClickListener {
            EventManager.getInstance().post(1, bd.tvContent.text.toString())
        }
        val random = Random()
        bd.btnPriorityQueue.setOnClickListener {
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
            bd.tvResult.text = s
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventManager.getInstance().unRegister(this)
    }
}
