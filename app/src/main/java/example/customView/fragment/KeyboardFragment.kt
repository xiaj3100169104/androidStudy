package example.customView.fragment

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.style.base.BaseFragment
import com.style.framework.R
import example.customView.SuspendWindowActivity

import example.customView.popupwindow.CustomKeyboardWindow
import kotlinx.android.synthetic.main.activity_keyboard.*


class KeyboardFragment : BaseFragment() {

    private var mLoginPop: CustomKeyboardWindow? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_keyboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view_custom_keyboard.setOnClickListener { v -> showCustom(v) }
        view_suspend.setOnClickListener { skip(SuspendWindowActivity::class.java) }

    }

    fun showCustom(cbLogin: View) {
        if (mLoginPop == null) {
            mLoginPop = CustomKeyboardWindow.Builder(context).create()
            //某些版本手机上不设置这个属性点击窗口外和返回键没反应
            mLoginPop!!.setBackgroundDrawable(ColorDrawable())
            mLoginPop!!.bt_positive.setOnClickListener {
                val number = mLoginPop!!.number
                mLoginPop!!.dismiss()
            }
        }
        mLoginPop!!.number = "6546"
        mLoginPop!!.showAsDropDown(cbLogin, 10, 10)
    }

}
