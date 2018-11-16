package example.custom_view.fragment

import android.view.View
import android.widget.Button

import com.style.base.BaseFragment
import com.style.framework.R
import com.style.view.progressbar.WaterPoloProgress

class WaterPoloFragment : BaseFragment() {

    private var mButton: Button? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_water_polo
    }

    override fun initData() {

        val sineCurve = view!!.findViewById<View>(R.id.custom_view) as WaterPoloProgress

        mButton = view!!.findViewById<View>(R.id.button) as Button
        mButton!!.setOnClickListener {
            val n = (Math.random() * 100).toInt()

            sineCurve.setPercentWithAnimation(n.toFloat())
        }
    }

}
