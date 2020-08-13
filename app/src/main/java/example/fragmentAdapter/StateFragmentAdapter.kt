package example.fragmentAdapter

import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import android.view.ViewGroup

/**
 *保存fragment数据状态（实际指arguments中的数据），对limit外的page进行回收，会销毁fragment实例(视图也肯定销毁了)，节约内存。
 * 适用场景：大量占内存fragment(比如视图中有图片),不适用于setArguments之外传递数据。
 */
class StateFragmentAdapter : androidx.fragment.app.FragmentStatePagerAdapter {
    private val titles: ArrayList<String>
    private var fragments: ArrayList<StateFragment>

    constructor(fm: androidx.fragment.app.FragmentManager, fragments: ArrayList<StateFragment>, titles: ArrayList<String>) : super(fm) {
        this.titles = titles
        this.fragments = fragments
    }

    //可以传递fragment刚创建时需要的数据
    override fun getItem(position: Int): StateFragment {
        return fragments[position]
    }

    private lateinit var mCurrentItem: StateFragment

    /**
     * 该方法会调两次不知为何。
     */
    override fun setPrimaryItem(container: ViewGroup, position: Int, `object`: Any) {
        mCurrentItem = `object` as StateFragment
        super.setPrimaryItem(container, position, `object`)
    }

    override fun getCount(): Int {
        return titles.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return titles[position % titles.size]
    }

    //调用notifyDataSetChanged激活此方法，返回PagerAdapter.POSITION_NONE,viewPager会调用destroyItem方法销毁，并且重新生成，加大系统开销
    //可能会影响性能,会造成卡顿
    /*override fun getItemPosition(`object`: Any): Int {
        return PagerAdapter.POSITION_NONE//激活getItem
    }*/
    /**
    viewpager的页面是不固定的，理论上可以是非常多的，这时候就要使用FragmentStatePagerAdapter来优化体验了，
    但是更新数据时，大多是只是更新一些页面上一些空间的显示，直接调用notifyDataSetChanged，重建页面有点太浪费了，
    于是我就想了一个简单的解决方案，在这里和大家分享一下，大家有更好的方法话可以提出来，共同学习。
    基本思路就是在更新的时候，拿到当前页面以及前一个和后一个页面的fragment，然后调用fragment中我们的某些更新方法，进行页面的刷新。
    这样就不用notifyDataSetChanged()，直接更新控件即可。
     */
}