package example.viewPagerBanner;

/*
 *                   _ooOoo_
 *                  o8888888o
 *                  88" . "88
 *                  (| -_- |)
 *                  O\  =  /O
 *               ____/`---'\____
 *             .'  \\|     |//  `.
 *            /  \\|||  :  |||//  \
 *           /  _||||| -:- |||||-  \
 *           |   | \\\  -  /// |   |
 *           | \_|  ''\---/''  |   |
 *           \  .-\__  `-`  ___/-. /
 *         ___`. .'  /--.--\  `. . __
 *      ."" '<  `.___\_<|>_/___.'  >'"".
 *     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *     \  \ `-.   \_ __\ /__ _/   .-` /  /
 *======`-.____`-.___\_____/___.-`____.-'======
 *                   `=---='
 *^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *         佛祖保佑       永无BUG
 */
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.github.chrisbanes.photoview.PhotoView
import com.style.base.BaseActivity
import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.R
import com.style.framework.databinding.AdapterIndicatorBinding
import com.style.framework.databinding.BannerActivityBinding

class BannerActivity : BaseActivity() {

    private lateinit var bd: BannerActivityBinding
    private lateinit var bannerFrags: ArrayList<BannerFragment>
    private lateinit var fAdapter: BannerAdapter
    private var mPageIndex: Int = 0

    private val fadeIn = R.anim.fade_in
    private val fadeOut = R.anim.fade_out

    override fun setRequestedOrientation(requestedOrientation: Int) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O)
            return
        super.setRequestedOrientation(requestedOrientation)
    }

    override fun onStart() {
        overridePendingTransition(fadeIn, fadeOut)
        super.onStart()
    }

    override fun onPause() {
        overridePendingTransition(fadeIn, fadeOut)
        super.onPause()
    }

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        bd = BannerActivityBinding.inflate(layoutInflater)
        setContentView(bd.root)
        setFullScreenStableDarkMode(false)

        //不知为何创建活动时设置会造成后面改变窗口透明度异常,目前只能在主题配置里面初始化窗口背景颜色.
        //setWindowAlpha(255)
        bannerFrags = arrayListOf()
        bannerFrags.add(BannerFragment.newInstance(R.mipmap.home_banner_1))
        bannerFrags.add(BannerFragment.newInstance(R.mipmap.home_banner_2))
        bannerFrags.add(BannerFragment.newInstance(R.mipmap.home_banner_3))
        fAdapter = BannerAdapter(this, bannerFrags)
        bd.viewPager2.adapter = fAdapter
        var list: ArrayList<Boolean> = arrayListOf()
        bannerFrags.forEach { list.add(false) }
        val mIndicatorAdapter = IndicatorAdapter(this, list)
        //不设置点击事件
        bd.rcvIndicator.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        bd.rcvIndicator.adapter = mIndicatorAdapter
        mIndicatorAdapter.setSelected(0)
        bd.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                mPageIndex = p0
                mIndicatorAdapter.setSelected(p0)
                resetPhotoView()
            }
        })
    }

    private fun resetPhotoView() {
        val childCount = bd.viewPager2.childCount
        for (i in 0 until childCount) {
            //获取当前页面的view
            val child = bd.viewPager2.getChildAt(i)
            //获取当前页面中的PhotoView
            val photoView2 = child.findViewById<PhotoView>(R.id.iv_banner)
            if (photoView2 != null) {
                //获取photoView创建的PhotoViewAttacher
                val photoViewAttacher = photoView2.attacher
                //通过photoViewAttacher设置缩放大小
                //第一个参数是获取photoViewAttacher自带的缩放大小最小值，第二个和第三个参数设置缩放中心
                photoViewAttacher.setScale(photoViewAttacher.minimumScale, 0f, 0f, true)
            }
        }
    }

    class BannerAdapter : FragmentStateAdapter {

        private val list_fragment: ArrayList<BannerFragment>

        constructor(fa: FragmentActivity, list_fragment: ArrayList<BannerFragment>) : super(fa) {
            this.list_fragment = list_fragment
        }

        override fun getItemCount(): Int {
            return list_fragment.size
        }

        override fun createFragment(position: Int): Fragment {
            return list_fragment[position]
        }
    }

    class IndicatorAdapter : BaseRecyclerViewAdapter<Boolean> {

        constructor(context: Context?, dataList: ArrayList<Boolean>) : super(context, dataList)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val bd: AdapterIndicatorBinding = AdapterIndicatorBinding.inflate(layoutInflater, parent, false)
            return ViewHolder(bd)
        }

        override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
            val holder = viewHolder as ViewHolder
            val f = list[position]
            holder.bd.viewIndicator.isSelected = f
            super.setOnItemClickListener(holder.itemView, position)
        }

        fun setSelected(i: Int) {
            for (k in list.indices)
                list[k] = k == i
            notifyDataSetChanged()
        }

        class ViewHolder : RecyclerView.ViewHolder {
            var bd: AdapterIndicatorBinding

            constructor(bd: AdapterIndicatorBinding) : super(bd.root) {
                this.bd = bd
            }
        }
    }
}