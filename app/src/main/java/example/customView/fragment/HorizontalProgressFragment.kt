package example.customView.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseFragment
import com.style.framework.databinding.ActivityHorizontalProgressBinding


class HorizontalProgressFragment : BaseFragment() {

    private lateinit var bd: ActivityHorizontalProgressBinding

    internal var handler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val p = msg.data.getInt("progress")
            when (msg.what) {
                1 -> bd.progress.setProgress(p)
                2 -> bd.progress2.setProgress(p)
                3 -> bd.progress3.setProgress(p)
                4 -> bd.progress4.setProgress(p)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bd = ActivityHorizontalProgressBinding.inflate(inflater, container, false)
        return bd.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bd.progress.setProgressColor(Color.BLACK)
        bd.progress.setProgress(100)
        bd.btDownload.setOnClickListener { down() }

    }

    protected fun down() {
        val downloadThread = DownloadThread(1, 100)
        val downloadThread2 = DownloadThread(2, 50)
        val downloadThread3 = DownloadThread(3, 20)
        val downloadThread4 = DownloadThread(4, 10)
        downloadThread.start()
        downloadThread2.start()
        downloadThread3.start()
        downloadThread4.start()
    }


    private inner class DownloadThread(private val url: Int, private val sleepTime: Long) : Thread() {

        override fun run() {
            super.run()
            for (i in 0..100) {
                val msg = handler.obtainMessage(url)
                val b = Bundle()
                b.putInt("progress", i)
                msg.data = b
                handler.sendMessage(msg)
                try {
                    Thread.sleep(sleepTime)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }
        }
    }

}
