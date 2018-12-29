package example.viewPagerBanner

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by xiajun on 2018/1/6.
 */

class BannerAdapter : FragmentPagerAdapter {

    private val list_fragment: List<Fragment>

    constructor(fm: FragmentManager, list_fragment: List<Fragment>) : super(fm) {
        this.list_fragment = list_fragment
    }

    override fun getItem(position: Int): Fragment {
        return list_fragment[position]
    }

    override fun getCount(): Int {
        return list_fragment.size
    }
}
