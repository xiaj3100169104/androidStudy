package example.fragmentAdapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

/**
 *不会销毁fragment实例，只销毁视图，
 * 使用场景：少量固定的几个fragment，既然是少量就使用viewPager.offscreenPageLimit避免fragment销毁再重建view，直接刷新fragment里面的view。
 * 若宿主activity加载时间过长可采用懒刷新(fragment显示时再刷新视图)。
 */
class IndexFragmentAdapter : androidx.fragment.app.FragmentPagerAdapter {
    private val fragments: List<IndexFragment>
    private val titles: List<String>

    constructor(fm: androidx.fragment.app.FragmentManager, fragments: List<IndexFragment>, titles: List<String>) : super(fm) {
        this.fragments = fragments
        this.titles = titles
    }

    /**
     * 不会每次调用，FragmentPagerAdapter缓存不存在时才调用。
     */
    override fun getItem(position: Int): IndexFragment {
        return fragments[position]
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position % titles.size]
    }
}