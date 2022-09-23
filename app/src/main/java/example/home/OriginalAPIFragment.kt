package example.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseNoPagerLazyRefreshFragment
import com.style.entity.KuaiDi
import com.style.framework.databinding.FragmentHome3Binding
import com.style.service.remote.RemotePlayActivity
import example.activity.AnimatorActivity
import example.activity.JniTestActivity
import example.activity.ReadAssetsActivity
import example.aidl.AidlActivity
import example.db.TestRoomActivity
import example.encrypt.EncryptActivity
import example.filedown.FileDownActivity
import example.location.LocationActivity
import example.music.MusicListActivity
import example.subThreadLooper.MsgToSubActivity
import example.web.WebViewActivity
import example.web.WebViewAndJSActivity
import kotlin.concurrent.thread


class OriginalAPIFragment : BaseNoPagerLazyRefreshFragment() {

    private lateinit var bd: FragmentHome3Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bd = FragmentHome3Binding.inflate(inflater, container, false)
        return bd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bd.viewMainMsgToSub.setOnClickListener { skip(MsgToSubActivity::class.java) }
        bd.viewTestRoom.setOnClickListener { skip(TestRoomActivity::class.java) }
        bd.viewAnimator.setOnClickListener { skip(AnimatorActivity::class.java) }
        bd.viewRemoteWeb.setOnClickListener { skip(WebViewActivity::class.java) }
        bd.viewWebWithJs.setOnClickListener { skip(WebViewAndJSActivity::class.java) }
        bd.viewJni.setOnClickListener { skip(JniTestActivity::class.java) }
        bd.viewAidl.setOnClickListener { skip(AidlActivity::class.java) }
        bd.viewLocation.setOnClickListener { skip(LocationActivity::class.java) }
        bd.viewReadAssets.setOnClickListener { skip(ReadAssetsActivity::class.java) }
        bd.viewEncrypt.setOnClickListener { skip(EncryptActivity::class.java) }
        bd.viewFileDown.setOnClickListener { skip(FileDownActivity::class.java) }
        bd.viewVoice.setOnClickListener { skip(MusicListActivity::class.java) }
        bd.viewOtherProcess.setOnClickListener { skip(RemotePlayActivity::class.java) }
    }
}
