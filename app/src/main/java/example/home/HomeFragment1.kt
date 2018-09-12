package example.home

import android.view.View
import com.style.base.BaseFragment
import com.style.framework.R
import com.style.framework.databinding.FragmentHome1Binding
import example.activity.MyRadioGroupActivity
import example.customview.*
import example.tablayout.TabLayoutActivity

class HomeFragment1 : BaseFragment() {

    private lateinit var bd: FragmentHome1Binding

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_1
    }

    override fun initData() {
        bd = getBinding()
        bd.refreshLayout.setOnRefreshListener { refreshlayout ->
            refreshlayout.finishRefresh(2000/*,false*/)//传入false表示刷新失败
        }
        bd.refreshLayout.setOnLoadMoreListener { refreshLayout ->
            refreshLayout.finishLoadMore(2000/*,false*/)//传入false表示加载失败
        }
        bd.btnSystemWidget.setOnClickListener { skip(TabLayoutActivity::class.java) }
        bd.btnRadioGroup.setOnClickListener { skip(MyRadioGroupActivity::class.java) }
        bd.viewWriteWord.setOnClickListener { skip(WriteWordActivity::class.java) }
        bd.viewSuspend.setOnClickListener { skip(SuspendWindowActivity::class.java) }
        bd.btnCustomView.setOnClickListener { skip(CustomViewMainActivity::class.java) }
        bd.btnHeart.setOnClickListener { skip(HeartLineActivity::class.java) }
        bd.btnTemp.setOnClickListener { skip(TempActivity::class.java) }
        bd.btnBp.setOnClickListener { skip(BpActivity::class.java) }
        bd.btnSleep.setOnClickListener { skip(SleepWeekActivity::class.java) }

    }
}