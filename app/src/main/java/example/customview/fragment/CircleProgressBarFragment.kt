package example.customview.fragment

import com.style.base.BaseFragment
import com.style.framework.R
import com.style.framework.databinding.ActivityCircleProgressBinding


class CircleProgressBarFragment : BaseFragment() {

    private lateinit var bd: ActivityCircleProgressBinding

    override fun getLayoutResId(): Int {
        return R.layout.activity_circle_progress
    }

    override fun initData() {
        bd = getBinding()

        bd.button.setOnClickListener { v ->
            val n = (Math.random() * 100).toInt()
            bd.progressBar.progress = n
            bd.circleProgress.progress = n
            bd.arcEnergy.progress = n
        }
        bd.button2.setOnClickListener { v ->
            val n = (Math.random() * 100).toInt()
            bd.progressBar.setPercentWithAnimation(n)
            bd.circleProgress.setPercentWithAnimation(n)
            bd.arcEnergy.setPercentWithAnimation(n)
        }

    }

}
