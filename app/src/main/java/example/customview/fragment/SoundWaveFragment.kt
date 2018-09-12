package example.customview.fragment

import android.os.Handler
import android.view.View

import com.style.base.BaseFragment
import com.style.framework.R
import com.style.view.SoundWaveView

class SoundWaveFragment : BaseFragment() {

    override fun getLayoutResId(): Int {
        return R.layout.activity_sound_wave
    }

    override fun initData() {

        val sineCurve = view!!.findViewById<View>(R.id.custom_view2) as SoundWaveView
        Handler().postDelayed({ sineCurve.startAnimation() }, 1000)
    }

}
