package example.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import example.activity.GreenDaoActivity
import example.album.GlideDealActivity
import example.activity.TestRxActivity

import com.style.base.BaseFragment
import com.style.framework.R
import com.style.framework.databinding.FragmentHome4Binding


import example.ble.BLEActivity
import example.ble.BlueToothActivity
import example.media.socket.chat.SocketTestActivity
import example.media.AudioRecordActivity
import example.queue.QueueTestActivity
import example.media.VideoTestActivity
import example.webservice.WebServiceActivity


class HomeFragment4 : BaseFragment() {

    private lateinit var bd: FragmentHome4Binding

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_4
    }

    override fun initData() {
        bd = getBinding()
        bd.event = EventListener()
        logE(TAG, "initData")

    }

    inner class EventListener {

        fun testRX(v: View) {
            skip(TestRxActivity::class.java)
        }

        fun testGreenDao(v: View) {
            skip(GreenDaoActivity::class.java)
        }

        fun testGlide(v: View) {
            skip(GlideDealActivity::class.java)
        }

        fun testAppCrash(v: View) {
            val test: String? = null
            logE(TAG, test!!.toString())
        }

        fun skip46(v: View) {
            skip(VideoTestActivity::class.java)
        }

        fun skip48(v: View) {
            skip(AudioRecordActivity::class.java)
        }

        fun skip49(v: View) {
            skip(SocketTestActivity::class.java)
        }

        fun skip491(v: View) {
            skip(QueueTestActivity::class.java)
        }

        fun skip492(v: View) {
            skip(BlueToothActivity::class.java)
        }

        fun skip493(v: View) {
            skip(BLEActivity::class.java)
        }

        fun skip11(v: View) {
            skip(WebServiceActivity::class.java)
        }

    }
}
