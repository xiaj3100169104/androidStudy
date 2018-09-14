package example.home

import android.view.View
import com.style.base.BaseFragment
import com.style.framework.R
import com.style.framework.databinding.FragmentHomeGestureBinding
import example.drag.DragActivity
import example.drag.SwipeMenuActivity
import example.gesture.DispatchGestureActivity
import example.gesture.SimpleGestureActivity
import example.gesture.TestGestureActivity
import example.softInput.StatusBarStyleMainActivity

class GestureFragment : BaseFragment() {

    private lateinit var bd: FragmentHomeGestureBinding

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_gesture
    }

    override fun initData() {
        bd = getBinding()
        bd.onItemClickListener = OnItemClickListener()
        bd.refreshLayout.setOnRefreshListener { refreshlayout ->
            refreshlayout.finishRefresh(2000/*,false*/)//传入false表示刷新失败
        }
        bd.refreshLayout.setOnLoadMoreListener { refreshLayout ->
            refreshLayout.finishLoadMore(2000/*,false*/)//传入false表示加载失败
        }
        bd.btnSwipeMenu.setOnClickListener { skip(SwipeMenuActivity::class.java) }
    }

    inner class OnItemClickListener {

        fun skip9(v: View) {
            skip(SimpleGestureActivity::class.java)
        }

        fun skip91(v: View) {
            skip(DispatchGestureActivity::class.java)
        }

        fun skip10(v: View) {
            skip(TestGestureActivity::class.java)
        }

        fun skip11(v: View) {
            skip(StatusBarStyleMainActivity::class.java)
        }

        fun skip13(v: View) {
            skip(DragActivity::class.java)
        }

    }
}