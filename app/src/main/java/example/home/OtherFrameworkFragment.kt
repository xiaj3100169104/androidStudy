package example.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseNoPagerLazyRefreshFragment
import com.style.framework.databinding.FragmentHome4Binding
import example.activity.GlideDealActivity
import example.activity.PreviewActivity
import example.activity.QRCodeActivity
import example.media.AudioRecordActivity
import example.media.VideoTestActivity
import example.media.socket.chat.SocketTestActivity
import example.queue.QueueTestActivity
import example.web_service.CoroutineActivity
import example.web_service.WebServiceActivity
import example.wifi.WifiTestActivity


class OtherFrameworkFragment : BaseNoPagerLazyRefreshFragment() {

    private lateinit var bd: FragmentHome4Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bd = FragmentHome4Binding.inflate(inflater, container, false)
        return bd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bd.viewQrCode.setOnClickListener { skip(QRCodeActivity::class.java) }
        bd.viewGlideDeal.setOnClickListener { skip(GlideDealActivity::class.java) }
        bd.viewAppCrash.setOnClickListener {
            val test: String? = null
            logE(TAG, test!!.toString())
        }
        bd.viewCamera2.setOnClickListener { skip(PreviewActivity::class.java) }
        bd.viewVoiceRecord.setOnClickListener { skip(VideoTestActivity::class.java) }
        bd.viewVoiceRecord.setOnClickListener { skip(AudioRecordActivity::class.java) }
        bd.viewSocket.setOnClickListener { skip(SocketTestActivity::class.java) }
        bd.viewEventManager.setOnClickListener { skip(QueueTestActivity::class.java) }
        bd.viewWifi.setOnClickListener { skip(WifiTestActivity::class.java) }
        bd.viewWebservice.setOnClickListener { skip(WebServiceActivity::class.java) }
        bd.viewCoroutine.setOnClickListener { skip(CoroutineActivity::class.java) }

    }
}
