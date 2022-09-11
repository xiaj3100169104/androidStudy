package example.fragmentAdapter;

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.style.base.BaseTitleBarActivity
import com.style.framework.databinding.TablayoutWithViewpager2ActivityBinding

class TabViewPager2Activity : BaseTitleBarActivity() {

    private lateinit var bd: TablayoutWithViewpager2ActivityBinding
    private lateinit var fAdapter: IndexFragmentAdapter
    private val fragments = ArrayList<TabFragment>()
    private val titles = ArrayList<String>()

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        bd = TablayoutWithViewpager2ActivityBinding.inflate(layoutInflater)
        setContentView(bd.root)
        setFullScreenStableDarkMode(true)
        setTitleBarTitle("FragmentStateAdapter")
        for (i in 0..4) {
            titles.add(i.toString())
            fragments.add(TabFragment.newInstance(i))
        }
        fAdapter = IndexFragmentAdapter(this, fragments,)
        bd.viewPager2.adapter = fAdapter
        //TabLayout与ViewPager2联动,必须要在添加适配器完了之后才能新建该方法.
         TabLayoutMediator(bd.tabLayout, bd.viewPager2,  TabLayoutMediator.TabConfigurationStrategy { tab, position ->
             tab.text = titles[position]
         }).attach();
    }

    class IndexFragmentAdapter : FragmentStateAdapter {
        private val fragments: ArrayList<TabFragment>

        constructor(fa: FragmentActivity, fragments: ArrayList<TabFragment>) : super(fa) {
            this.fragments = fragments
        }

        override fun getItemCount(): Int {
            return fragments.size
        }

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }
    }
}
