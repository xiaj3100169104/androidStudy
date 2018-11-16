package example.home

import example.activity.GlideDealActivity
import example.activity.TestRxActivity

import com.style.base.BaseFragment
import com.style.framework.R

import example.ble.BLEActivity
import example.ble.BlueToothActivity
import example.media.socket.chat.SocketTestActivity
import example.media.AudioRecordActivity
import example.queue.QueueTestActivity
import example.media.VideoTestActivity
import example.web_service.WebServiceActivity
import kotlinx.android.synthetic.main.fragment_home_4.*


class OtherFrameworkFragment : BaseFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_4
    }

    override fun initData() {
        view_rx_java.setOnClickListener { skip(TestRxActivity::class.java) }
        view_glide_deal.setOnClickListener { skip(GlideDealActivity::class.java) }
        view_app_crash.setOnClickListener {
            val test: String? = null
            logE(TAG, test!!.toString())
        }
        view_video_record.setOnClickListener { skip(VideoTestActivity::class.java) }
        view_voice_record.setOnClickListener { skip(AudioRecordActivity::class.java) }
        view_socket.setOnClickListener { skip(SocketTestActivity::class.java) }
        view_event_manager.setOnClickListener { skip(QueueTestActivity::class.java) }
        view_blue_tooth.setOnClickListener { skip(BlueToothActivity::class.java) }
        view_ble.setOnClickListener { skip(BLEActivity::class.java) }
        view_webservice.setOnClickListener { skip(WebServiceActivity::class.java) }

    }
}
