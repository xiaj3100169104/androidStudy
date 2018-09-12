package example.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.style.base.BaseFragment
import com.style.bean.User
import com.style.data.prefs.AccountManager
import com.style.framework.R
import com.style.framework.databinding.FragmentHome3Binding

import example.activity.AnimatorActivity
import example.activity.DataBindingActivity
import example.activity.MsgToSubActivity
import example.activity.ReadAssetsActivity
import example.activity.TestDBActivity
import example.aidl.AidlActivity
import example.db.EncryptActivity
import example.filedown.FileDownActivity
import example.music.MusicListActivity
import example.music.remote.RemotePlayActivity
import example.ndk.JniTestActivity
import example.web.WebViewActivity
import example.web.WebViewAndJSActivity
import example.web.WebViewFeedbackActivity


class HomeFragment3 : BaseFragment() {
    private lateinit var bd: FragmentHome3Binding

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_3
    }

    override fun initData() {
        bd = getBinding()
        bd.onItemClickListener = OnItemClickListener()
        bd.refreshLayout.isEnableLoadMore = false
    }

    inner class OnItemClickListener {
        fun ski(v: View) {
            skip(MsgToSubActivity::class.java)
        }

        fun skip418(v: View) {
            skip(TestDBActivity::class.java)
        }

        fun skip419(v: View) {
            skip(DataBindingActivity::class.java)
        }


        fun skip414(v: View) {
            skip(AnimatorActivity::class.java)
        }

        fun skip7(v: View) {
            skip(WebViewActivity::class.java)
        }

        fun skip8(v: View) {
            skip(WebViewAndJSActivity::class.java)
        }

        fun skip81(v: View) {
            skip(WebViewFeedbackActivity::class.java)
        }

        fun skip9(v: View) {
            skip(JniTestActivity::class.java)
        }

        fun skip10(v: View) {
            skip(AidlActivity::class.java)
        }

        fun skip11(v: View) {
            skip(ReadAssetsActivity::class.java)
        }

        fun skip12(v: View) {
            skip(EncryptActivity::class.java)
        }

        fun skip6(v: View) {
            skip(FileDownActivity::class.java)
        }

        fun skip14(v: View) {
            skip(MusicListActivity::class.java)
        }

        fun skip15(v: View) {

            skip(RemotePlayActivity::class.java)

        }
    }
}
