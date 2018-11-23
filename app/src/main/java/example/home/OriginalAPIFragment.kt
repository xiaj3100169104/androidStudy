package example.home

import com.style.base.BaseFragment
import com.style.framework.R
import example.activity.AnimatorActivity
import example.activity.MsgToSubActivity
import example.activity.ReadAssetsActivity
import example.aidl.AidlActivity
import example.db.TestRoomActivity
import example.encrypt.EncryptActivity
import example.filedown.FileDownActivity
import example.music.MusicListActivity
import example.music.remote.RemotePlayActivity
import example.ndk.JniTestActivity
import example.web.WebViewActivity
import example.web.WebViewAndJSActivity
import example.web.WebViewFeedbackActivity
import kotlinx.android.synthetic.main.fragment_home_3.*


class OriginalAPIFragment : BaseFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_3
    }

    override fun initData() {
        view_main_msg_to_sub.setOnClickListener { skip(MsgToSubActivity::class.java) }
        view_test_room.setOnClickListener { skip(TestRoomActivity::class.java) }
        view_animator.setOnClickListener { skip(AnimatorActivity::class.java) }
        view_remote_web.setOnClickListener { skip(WebViewActivity::class.java) }
        view_web_with_js.setOnClickListener { skip(WebViewAndJSActivity::class.java) }
        view_feedback.setOnClickListener { skip(WebViewFeedbackActivity::class.java) }
        view_jni.setOnClickListener { skip(JniTestActivity::class.java) }
        view_aidl.setOnClickListener { skip(AidlActivity::class.java) }
        view_read_assets.setOnClickListener { skip(ReadAssetsActivity::class.java) }
        view_encrypt.setOnClickListener { skip(EncryptActivity::class.java) }
        view_file_down.setOnClickListener { skip(FileDownActivity::class.java) }
        view_voice.setOnClickListener { skip(MusicListActivity::class.java) }
        view_other_process.setOnClickListener { skip(RemotePlayActivity::class.java) }
    }
}
