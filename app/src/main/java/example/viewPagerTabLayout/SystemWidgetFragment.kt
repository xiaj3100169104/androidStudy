package example.viewPagerTabLayout

import com.style.base.BaseFragment
import com.style.framework.R
import example.address.AddressActivity
import example.album.SelectLocalPictureActivity
import example.dialog.DialogActivity
import example.dialog.WheelActivity
import kotlinx.android.synthetic.main.fragment_system_widget.*

class SystemWidgetFragment : BaseFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.fragment_system_widget
    }

    override fun initData() {
        btn_album.setOnClickListener { skip(SelectLocalPictureActivity::class.java) }
        btn_address.setOnClickListener { skip(AddressActivity::class.java) }
        btn_dialog.setOnClickListener { skip(DialogActivity::class.java) }
        btn_wheel.setOnClickListener { skip(WheelActivity::class.java) }
    }
}