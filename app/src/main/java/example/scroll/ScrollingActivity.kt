package example.scroll

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.style.framework.databinding.ActivityScrollingBinding

/**
 * app:layout_scrollFlags，设置为：scroll|enterAlways|snap 便是指定标题栏随屏幕滚动实现的属性。
 * scroll表示屏幕向上滑动时，标题栏同时向上滑动并隐藏；
 * enterAlways表示屏幕向下滑动时，标题栏同时向下活动并显示；
 * snap表示Toolbar没有完全显示或隐藏时，根据滚动距离，自动选择。
 */
class ScrollingActivity : AppCompatActivity() {

    private lateinit var bd: ActivityScrollingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bd = ActivityScrollingBinding.inflate(layoutInflater)
        setContentView(bd.root)

    }
}
