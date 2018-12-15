package example.customView.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import com.style.base.BaseFragment
import com.style.framework.R
import com.style.view.progressbar.WaterPoloProgress

class WaterPoloFragment : BaseFragment() {

    private var mButton: Button? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_water_polo, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sineCurve = view!!.findViewById<View>(R.id.custom_view) as WaterPoloProgress

        mButton = view!!.findViewById<View>(R.id.button) as Button
        mButton!!.setOnClickListener {
            val n = (Math.random() * 100).toInt()

            sineCurve.setPercentWithAnimation(n.toFloat())
        }
    }

}
