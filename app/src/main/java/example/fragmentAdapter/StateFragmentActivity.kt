package example.fragmentAdapter;

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.view.ViewPager
import com.style.base.activity.BaseTitleBarActivity
import com.style.framework.R
import kotlinx.android.synthetic.main.fragment_adapter_activity.*

class StateFragmentActivity : BaseTitleBarActivity() {
    private lateinit var fAdapter: StateFragmentAdapter
    private val fragments = ArrayList<StateFragment>()

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.fragment_adapter_activity)
        setFullScreenStableDarkMode(true)

        initData()
    }

    private lateinit var mViewModel: StateFragmentActivityViewModel

    fun initData() {
        setTitleBarTitle("fragmentStatePagerAdapter")
        mViewModel = getViewModel(StateFragmentActivityViewModel::class.java)
        val titles = mViewModel.getTitleData()
        val data = mViewModel.datas
        titles.forEachIndexed { index, s ->
            fragments.add(StateFragment.newInstance("title".plus(data[s].toString())))
        }
        fAdapter = StateFragmentAdapter(supportFragmentManager, fragments, titles)
        viewPager.adapter = fAdapter
        tabLayout.setupWithViewPager(viewPager, true)
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
