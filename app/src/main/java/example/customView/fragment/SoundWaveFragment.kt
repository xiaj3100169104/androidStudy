package example.customView.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseFragment
import com.style.framework.databinding.ActivitySoundWaveBinding

class SoundWaveFragment : BaseFragment() {

    private lateinit var bd: ActivitySoundWaveBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bd = ActivitySoundWaveBinding.inflate(inflater, container, false)
        return bd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bd.btnStart.setOnClickListener { bd.customView2.start() }
        bd.btnPause.setOnClickListener { bd.customView2.pause() }
        bd.btnReset.setOnClickListener { bd.customView2.reset() }

    }

}
