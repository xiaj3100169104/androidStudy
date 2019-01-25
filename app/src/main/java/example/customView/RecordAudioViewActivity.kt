package example.customView;

import android.os.Bundle
import com.style.base.BaseDefaultTitleBarActivity
import com.style.framework.R
import kotlinx.android.synthetic.main.activity_record_audio_view.*
import java.util.*
import kotlin.concurrent.thread

class RecordAudioViewActivity : BaseDefaultTitleBarActivity() {

    //实际情况一般在7000以内
    var max = 32767
    var min = 0

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.activity_record_audio_view)
        btn_start_record.setOnClickListener { startRecord() }
    }

    private var mThread: Thread? = null

    private fun startRecord() {
        mThread = Thread {
            val random = Random()
            while (true) {
                try {
                    Thread.sleep(100)
                    val v = random.nextInt(7000).toFloat() / 7000
                    logE("RecordAudioViewActivity", v.toString())
                    record_audio_view.postValue(v)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                    break
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mThread?.interrupt()
    }

}
