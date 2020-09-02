package example.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import example.activity.GlideDealActivity
import example.activity.TestRxActivity

import com.style.base.BaseNoPagerLazyRefreshFragment
import com.style.framework.R
import example.activity.QRCodeActivity

import example.media.socket.chat.SocketTestActivity
import example.media.AudioRecordActivity
import example.queue.QueueTestActivity
import example.media.VideoTestActivity
import example.web_service.WebServiceActivity
import example.wifi.WifiTestActivity
import kotlinx.android.synthetic.main.fragment_home_4.*


class OtherFrameworkFragment : BaseNoPagerLazyRefreshFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_4, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_qr_code.setOnClickListener { skip(QRCodeActivity::class.java) }
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
        view_wifi.setOnClickListener { skip(WifiTestActivity::class.java) }
        view_webservice.setOnClickListener { skip(WebServiceActivity::class.java) }

    }
}
