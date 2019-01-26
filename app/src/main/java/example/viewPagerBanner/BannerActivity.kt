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
import android.os.Bundle
import android.os.Handler
import android.support.v4.view.ViewPager
import android.view.View
import com.github.chrisbanes.photoview.PhotoView
import com.github.chrisbanes.photoview.PhotoViewAttacher
import com.style.base.BaseTransparentStatusBarActivity
import com.style.framework.R
import kotlinx.android.synthetic.main.banner_activity.*

class BannerActivity : BaseTransparentStatusBarActivity() {
    private lateinit var bannerFrags: ArrayList<BannerFragment>
    private lateinit var fAdapter: BannerAdapter
    private var mPageIndex: Int = 0

    private val fadeIn = R.anim.fade_in
    private val fadeOut = R.anim.fade_out

    override fun setRequestedOrientation(requestedOrientation: Int) {
        return
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
        setContentView(R.layout.banner_activity)
        //不知为何启动时设置会造成后续改变窗口透明度异常,只有在主题配置里面初始化窗口背景颜色（无奈。。）
        //setWindowAlpha(255)
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
                mPageIndex = p0
                banner_group.changeSelected(mPageIndex)
                resetPhotoView()
            }
        })
        banner_group.changeSelected(mPageIndex)
        banner_group.setOnClickChildListener { p ->
            viewPager.setCurrentItem(p, false)
        }
    }

    private fun resetPhotoView() {
        val childCount = viewPager.childCount
        for (i in 0 until childCount) {
            //获取当前页面的view
            val child = viewPager.getChildAt(i)
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
}