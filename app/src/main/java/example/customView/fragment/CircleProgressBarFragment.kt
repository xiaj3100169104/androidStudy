package example.customView.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseFragment
import com.style.framework.R
import com.style.framework.databinding.ActivityCircleProgressBinding


class CircleProgressBarFragment : BaseFragment() {

    private lateinit var bd: ActivityCircleProgressBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_circle_progress, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bd = getBinding(view)

        bd.button.setOnClickListener { v ->
            val n = (Math.random() * 100).toInt()
            bd.progressBar.progress = n
            bd.circleProgress.progress = n
            bd.arcEnergy.progress = n
        }
        bd.button2.setOnClickListener { v ->
            val n = (Math.random() * 100).toInt()
            bd.progressBar.setProgressWithAnimation(n)
            bd.circleProgress.setProgressWithAnimation(n)
            bd.arcEnergy.setProgressWithAnimation(n)
        }

    }

}
