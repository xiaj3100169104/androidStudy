package example.viewPagerTabLayout

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by xiajun on 2018/1/6.
 */

class FindTabAdapter : FragmentPagerAdapter {//tab名的列表

    private val list_fragment: List<Fragment>
    private val list_Title: List<String>

    //fragment列表
    constructor(fm: FragmentManager, list_fragment: List<Fragment>, list_Title: List<String>) : super(fm) {
        this.list_fragment = list_fragment
        this.list_Title = list_Title
    }

    override fun getItem(position: Int): Fragment {
        return list_fragment[position]
    }

    override fun getCount(): Int {
        return list_Title.size
    }

    //此方法用来显示tab上的名字
    override fun getPageTitle(position: Int): CharSequence? {

        return list_Title[position % list_Title.size]
    }
}
