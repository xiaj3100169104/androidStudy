package example.customView

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class CustomViewFragmentAdapter : androidx.fragment.app.FragmentPagerAdapter {
    private val fragments: List<androidx.fragment.app.Fragment>
    private val titles: List<String>

    constructor(fm: androidx.fragment.app.FragmentManager, fragments: List<androidx.fragment.app.Fragment>, titles: List<String>) : super(fm) {
        this.fragments = fragments
        this.titles = titles
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
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
