package example.tablayout

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment

import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import com.style.framework.databinding.ActivityTabLayoutBinding

import java.util.ArrayList

class TabLayoutActivity : BaseTitleBarActivity() {

    private lateinit var bd: ActivityTabLayoutBinding
    private lateinit var fAdapter: FindTabAdapter                            //定义adapter
    private val fragments = ArrayList<Fragment>()                                //定义要装fragment的列表
    private val titles = ArrayList<String>()

    override fun getLayoutResId(): Int {
        return R.layout.activity_tab_layout
    }

    override fun initData() {
        bd = getBinding()
        setToolbarTitle("tabLayout")
        titles.add("系统挂件")
        fragments.add(SystemWidgetFragment())
        titles.add("title_0")
        fragments.add(TabLayoutFragment.newInstance("BottomSheetBehavior"))
        titles.add("title_1")
        fragments.add(BottomSheetDialogFragment.newInstance("BottomSheetDialog"))
        fAdapter = FindTabAdapter(this.supportFragmentManager, fragments, titles)
        bd.viewPager.adapter = fAdapter
        bd.tabLayout.setupWithViewPager(bd.viewPager)
        bd.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }
}
