package example.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseNoPagerLazyRefreshFragment
import com.style.framework.R
import com.style.framework.databinding.FragmentHome1Binding
import example.address.AddressActivity
import example.album.SelectLocalPictureActivity
import example.viewPagerBanner.BannerActivity
import example.customView.*
import example.dialog.DialogActivity
import example.dialog.WheelActivity
import example.gesture.XXRefreshActivity
import kotlinx.android.synthetic.main.fragment_home_1.*

class CustomViewFragment : BaseNoPagerLazyRefreshFragment() {

    private lateinit var bd: FragmentHome1Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bd = getBinding(view)
        btn_album.setOnClickListener { skip(SelectLocalPictureActivity::class.java) }
        btn_address.setOnClickListener { skip(AddressActivity::class.java) }
        btn_dialog.setOnClickListener { skip(DialogActivity::class.java) }
        btn_wheel.setOnClickListener { skip(WheelActivity::class.java) }
        view_suspend.setOnClickListener { skip(SuspendWindowActivity::class.java) }
        bd.btnRadioGroup.setOnClickListener { skip(BannerActivity::class.java) }
        bd.viewWriteWord.setOnClickListener { skip(WriteWordActivity::class.java) }
        bd.btnCustomView.setOnClickListener { skip(CustomViewMainActivity::class.java) }
        bd.btnHeart.setOnClickListener { skip(HeartLineActivity::class.java) }
        bd.btnTemp.setOnClickListener { skip(TempActivity::class.java) }
        bd.btnBp.setOnClickListener { skip(BpActivity::class.java) }
        bd.btnSleep.setOnClickListener { skip(SleepWeekActivity::class.java) }
        bd.btnSport.setOnClickListener { skip(SportWeekActivity::class.java) }
        bd.btnEcg.setOnClickListener { skip(EcgActivity::class.java) }
        bd.btnRecordAudioView.setOnClickListener { skip(RecordAudioViewActivity::class.java) }
        bd.btnExpandableText.setOnClickListener { skip(XXRefreshActivity::class.java) }
    }
}