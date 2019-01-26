package example.drag

import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.style.framework.R
import kotlinx.android.synthetic.main.activity_scrolling.*

/**
 * app:layout_scrollFlags，设置为：scroll|enterAlways|snap 便是指定标题栏随屏幕滚动实现的属性。
 * scroll表示屏幕向上滑动时，标题栏同时向上滑动并隐藏；
 * enterAlways表示屏幕向下滑动时，标题栏同时向下活动并显示；
 * snap表示Toolbar没有完全显示或隐藏时，根据滚动距离，自动选择。
 */
class ScrollingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*var visibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        window.decorView.systemUiVisibility = visibility
        //设置状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = resources.getColor(android.R.color.transparent)
        }*/
        setContentView(R.layout.activity_scrolling)
        //setSupportActionBar(toolbar)

    }
}
