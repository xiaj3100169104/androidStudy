package example.viewPagerTabLayout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseFragment
import com.style.framework.R
import example.address.AddressActivity
import example.album.SelectLocalPictureActivity
import example.dialog.DialogActivity
import example.dialog.WheelActivity
import kotlinx.android.synthetic.main.fragment_system_widget.*

class SystemWidgetFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_system_widget, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_album.setOnClickListener { skip(SelectLocalPictureActivity::class.java) }
        btn_address.setOnClickListener { skip(AddressActivity::class.java) }
        btn_dialog.setOnClickListener { skip(DialogActivity::class.java) }
        btn_wheel.setOnClickListener { skip(WheelActivity::class.java) }
    }
}