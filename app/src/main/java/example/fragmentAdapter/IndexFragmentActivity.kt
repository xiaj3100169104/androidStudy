package example.fragmentAdapter;

import android.os.Bundle
import android.support.v4.view.ViewPager
import com.style.base.activity.BaseTitleBarActivity
import com.style.framework.R
import kotlinx.android.synthetic.main.fragment_adapter_activity.*

class IndexFragmentActivity : BaseTitleBarActivity() {
    private lateinit var fAdapter: IndexFragmentAdapter
    private val fragments = ArrayList<IndexFragment>()
    private val titles = ArrayList<String>()

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.fragment_adapter_activity)
        setFullScreenStableDarkMode(true)
        setTitleBarTitle("fragmentPagerAdapter")
        for (i in 0..4) {
            titles.add(i.toString())
            fragments.add(IndexFragment.newInstance(i))
        }
        fAdapter = IndexFragmentAdapter(supportFragmentManager, fragments, titles)
        viewPager.adapter = fAdapter
        tabLayout.setupWithViewPager(viewPager, true)
        viewPager.offscreenPageLimit = fragments.size - 1
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {

            }

        })
        btn_change_fragment_index.setOnClickListener {

        }
        btn_change_fragment_next.setOnClickListener {

        }
    }

}
