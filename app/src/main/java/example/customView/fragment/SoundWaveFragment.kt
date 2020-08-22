package example.customView.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.style.base.BaseFragment
import com.style.framework.R
import kotlinx.android.synthetic.main.activity_sound_wave.*

class SoundWaveFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_sound_wave, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_start.setOnClickListener { custom_view2.start() }
        btn_pause.setOnClickListener { custom_view2.pause() }
        btn_reset.setOnClickListener { custom_view2.reset() }

    }

}
