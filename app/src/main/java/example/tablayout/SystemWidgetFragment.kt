package example.tablayout

import android.view.View
import com.style.base.BaseFragment
import com.style.framework.R
import com.style.framework.databinding.FragmentSystemWidgetBinding
import example.address.AddressActivity
import example.album.SelectLocalPictureActivity
import example.dialog.DialogActivity
import example.dialog.WheelActivity
import example.vlayout.MultiTypeActivity

class SystemWidgetFragment : BaseFragment() {

    private lateinit var bd: FragmentSystemWidgetBinding

    override fun getLayoutResId(): Int {
        return R.layout.fragment_system_widget
    }

    override fun initData() {
        bd = getBinding()
        bd.onItemClickListener = OnItemClickListener()
    }

    inner class OnItemClickListener {
        fun skip42(v: View) {
            skip(MultiTypeActivity::class.java)
        }

        fun skip1(v: View) {
            skip(SelectLocalPictureActivity::class.java)
        }

        fun skip2(v: View) {
            skip(AddressActivity::class.java)
        }

        fun skip31(v: View) {
            skip(DialogActivity::class.java)
        }

        fun skip4(v: View) {
            skip(WheelActivity::class.java)
        }
    }
}