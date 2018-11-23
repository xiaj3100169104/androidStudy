package example.home

import com.style.base.BaseFragment
import com.style.framework.R
import com.style.framework.databinding.FragmentHome1Binding
import example.radioGroup.MyRadioGroupActivity
import example.customView.*
import example.gesture.XXRefreshActivity
import example.tablayout.TabLayoutActivity

class CustomViewFragment : BaseFragment() {

    private lateinit var bd: FragmentHome1Binding

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_1
    }

    override fun initData() {
        bd = getBinding()
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