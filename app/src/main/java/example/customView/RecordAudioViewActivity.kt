package example.customView;

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import com.style.framework.databinding.ActivityRecordAudioViewBinding
import java.util.*

class RecordAudioViewActivity : BaseTitleBarActivity() {

    private lateinit var bd: ActivityRecordAudioViewBinding

    //实际情况一般在7000以内
    var max = 32767
    var min = 0

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        bd = ActivityRecordAudioViewBinding.inflate(layoutInflater)
        setContentView(bd.root)
        bd.tagsContainer.setOnClickChildListener { v ->
            val tagView = v as TextView
            showToast(tagView.text)
        }
        bd.btnStartRecord.setOnClickListener { startRecord() }
        bd.btnUpdateTags.setOnClickListener { updateTags() }
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
        bd.tagsContainer.setTags(tags)
    }

    private var mThread: Thread? = null

    private fun startRecord() {
        if (mThread != null && mThread?.isAlive!!)
            return
        mThread = Thread {
            val random = Random()
            while (true) {
                try {
                    Thread.sleep(60)
                    val v = random.nextInt(7000).toFloat() / 7000
                    logE("RecordAudioViewActivity", v.toString())
                    bd.recordAudioView.postValue(v)
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
