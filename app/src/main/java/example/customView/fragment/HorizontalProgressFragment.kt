package example.customView.fragment

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.style.base.BaseFragment
import com.style.framework.R
import com.style.view.progressbar.HorizontalProgressBar


class HorizontalProgressFragment : BaseFragment() {

    private var progressBar: HorizontalProgressBar? = null
    private var progressBar2: HorizontalProgressBar? = null
    private var progressBar3: HorizontalProgressBar? = null
    private var progressBar4: HorizontalProgressBar? = null

    internal var handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            val progress = msg.data.getInt("progress")
            when (msg.what) {
                1 -> progressBar!!.setProgress(progress)
                2 -> progressBar2!!.setProgress(progress)
                3 -> progressBar3!!.setProgress(progress)
                4 -> progressBar4!!.setProgress(progress)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_horizontal_progress, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view!!.findViewById<View>(R.id.progress) as HorizontalProgressBar
        progressBar2 = view!!.findViewById<View>(R.id.progress2) as HorizontalProgressBar
        progressBar3 = view!!.findViewById<View>(R.id.progress3) as HorizontalProgressBar
        progressBar4 = view!!.findViewById<View>(R.id.progress4) as HorizontalProgressBar

        progressBar!!.setProgressColor(Color.BLACK)
        progressBar!!.setProgress(100)
        view!!.findViewById<View>(R.id.bt_download).setOnClickListener { down() }

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
