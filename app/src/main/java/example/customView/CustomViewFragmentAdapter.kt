package example.customView

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class CustomViewFragmentAdapter : FragmentPagerAdapter {
    private val fragments: List<Fragment>
    private val titles: List<String>

    constructor(fm: FragmentManager, fragments: List<Fragment>, titles: List<String>) : super(fm) {
        this.fragments = fragments
        this.titles = titles
    }

    override fun getItem(position: Int): Fragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return titles.size
    }

    //此方法用来显示tab上的名字
    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position % titles.size]
    }
}
