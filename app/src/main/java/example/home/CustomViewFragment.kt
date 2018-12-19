package example.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseFragment
import com.style.base.BaseNoPagerLazyRefreshFragment
import com.style.framework.R
import com.style.framework.databinding.FragmentHome1Binding
import example.viewPagerBanner.MyRadioGroupActivity
import example.customView.*
import example.gesture.XXRefreshActivity
import example.viewPagerTabLayout.TabLayoutActivity

class CustomViewFragment : BaseNoPagerLazyRefreshFragment() {

    private lateinit var bd: FragmentHome1Binding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_1, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bd = getBinding(view)
        bd.btnSystemWidget.setOnClickListener { skip(TabLayoutActivity::class.java) }
        bd.btnRadioGroup.setOnClickListener { skip(MyRadioGroupActivity::class.java) }
        bd.viewWriteWord.setOnClickListener { skip(WriteWordActivity::class.java) }
        bd.btnCustomView.setOnClickListener { skip(CustomViewMainActivity::class.java) }
        bd.btnHeart.setOnClickListener { skip(HeartLineActivity::class.java) }
        bd.btnTemp.setOnClickListener { skip(TempActivity::class.java) }
        bd.btnBp.setOnClickListener { skip(BpActivity::class.java) }
        bd.btnSleep.setOnClickListener { skip(SleepWeekActivity::class.java) }
        bd.btnSport.setOnClickListener { skip(SportWeekActivity::class.java) }

        bd.btnEcg.setOnClickListener { skip(EcgActivity::class.java) }
        bd.btnExpandableText.setOnClickListener { skip(XXRefreshActivity::class.java) }


    }
}