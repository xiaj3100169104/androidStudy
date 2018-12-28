package example.viewPagerBanner;

import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.view.View
import com.style.base.BaseTransparentStatusBarActivity
import com.style.framework.R
import kotlinx.android.synthetic.main.banner_activity.*

class BannerActivity : BaseVerticalSlideFinishActivity() {
    private lateinit var bannerFrags: ArrayList<BannerFragment>
    private lateinit var fAdapter: BannerAdapter

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.banner_activity)
        //不知为何启动时设置会造成后续改变窗口透明度异常,只有在主题配置里面初始化窗口背景颜色（无奈。。）
        //setWindowAlpha(255)
        rootView = layout_root
        bannerFrags = ArrayList()
        bannerFrags.add(BannerFragment.newInstance(R.mipmap.home_banner_1))
        bannerFrags.add(BannerFragment.newInstance(R.mipmap.home_banner_2))
        bannerFrags.add(BannerFragment.newInstance(R.mipmap.home_banner_3))
        fAdapter = BannerAdapter(this.supportFragmentManager, bannerFrags)
        viewPager.adapter = fAdapter
        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {

            }

            override fun onPageSelected(p0: Int) {
                var id: Int = banner_group.getChildAt(p0).id
                banner_group.setCheckWithoutNotif(id)
            }
        })
        banner_group.setOnCheckedChangeListener { group, checkedId ->
            when (banner_group.checkedRadioButtonId) {
                banner_1.id -> viewPager.setCurrentItem(0, false)
                banner_2.id -> viewPager.setCurrentItem(1, false)
                banner_3.id -> viewPager.setCurrentItem(2, false)
            }
        }
    }
}