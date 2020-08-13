package example.viewPagerBanner

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 * Created by xiajun on 2018/1/6.
 */

class BannerAdapter : androidx.fragment.app.FragmentPagerAdapter {

    private val list_fragment: List<androidx.fragment.app.Fragment>

    constructor(fm: androidx.fragment.app.FragmentManager, list_fragment: List<androidx.fragment.app.Fragment>) : super(fm) {
        this.list_fragment = list_fragment
    }

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return list_fragment[position]
    }

    override fun getCount(): Int {
        return list_fragment.size
    }
}
