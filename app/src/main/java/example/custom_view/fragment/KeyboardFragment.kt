package example.custom_view.fragment

import android.graphics.drawable.ColorDrawable
import android.view.View

import com.style.base.BaseFragment
import com.style.framework.R

import example.custom_view.popupwindow.CustomKeyboardWindow


class KeyboardFragment : BaseFragment() {

    private var mLoginPop: CustomKeyboardWindow? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_keyboard
    }

    override fun initData() {

    }

    fun open(cbLogin: View) {
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
