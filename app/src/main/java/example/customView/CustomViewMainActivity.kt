package example.customView

import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import example.customView.fragment.*
import kotlinx.android.synthetic.main.custom_view_main.*
import java.util.*

class CustomViewMainActivity : BaseTitleBarActivity() {
    private lateinit var fAdapter: CustomViewFragmentAdapter
    private val fragments = ArrayList<androidx.fragment.app.Fragment>()
    private val titles = ArrayList<String>()

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.custom_view_main)
        setTitleBarTitle("tabLayout")
        titles.add("自定义饼状图")
        fragments.add(PieChartFragment())
        titles.add("自定义通知小圆点")
        fragments.add(CustomNotifyViewFragment())
        titles.add("圆环进度条")
        fragments.add(CircleProgressBarFragment())
        titles.add("波浪球")
        fragments.add(WaterPoloFragment())
        titles.add("声波")
        fragments.add(SoundWaveFragment())
        titles.add("水平进度")
        fragments.add(HorizontalProgressFragment())
        titles.add("扫描")
        fragments.add(ScanViewFragment())
        fAdapter = CustomViewFragmentAdapter(this.supportFragmentManager, fragments, titles)
        viewPager.adapter = fAdapter
        tabLayout.setupWithViewPager(viewPager)
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

}
