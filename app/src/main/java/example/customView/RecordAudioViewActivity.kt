package example.customView;

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import kotlinx.android.synthetic.main.activity_record_audio_view.*
import java.util.*

class RecordAudioViewActivity : BaseTitleBarActivity() {

    //实际情况一般在7000以内
    var max = 32767
    var min = 0

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.activity_record_audio_view)
        tags_container.setOnClickChildListener { v ->
            val tagView = v as TextView
            showToast(tagView.text)
        }
        btn_start_record.setOnClickListener { startRecord() }
        btn_update_tags.setOnClickListener { updateTags() }
    }

    private fun updateTags() {
        val list = arrayListOf("999999999", "88888888", "777777", "666666", "55555", "4444", "33333", "222222", "11111111", "00")
        var tagView: TextView
        val tags = arrayListOf<TextView>()
        list.forEachIndexed { index, s ->
            tagView = TextView(getContext())
            tagView.background = getContext().resources.getDrawable(R.drawable.bg_custom_tag)
            tagView.text = s
            tagView.setTextColor(Color.GRAY)
            tagView.textSize = 16f
            tagView.setPadding(20, 5, 20, 5)
            tags.add(tagView)
        }
        tags_container.setTags(tags)
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
        mThread?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        mThread?.interrupt()
    }

}
