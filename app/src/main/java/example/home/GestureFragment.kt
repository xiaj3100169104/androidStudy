package example.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseNoPagerLazyRefreshFragment
import com.style.framework.databinding.FragmentHomeGestureBinding
import example.activity.BottomSheetBehaviorActivity
import example.scroll.drag.DragActivity
import example.scroll.ScrollingActivity
import example.scroll.ScrollingParallaxActivity
import example.scroll.scroll_stop.ScrollingStopTopActivity
import example.scroll.swipeMenu.SwipeMenuActivity
import example.editLayout.EditLayoutChangeActivity
import example.fragmentAdapter.TabViewPager2Activity
import example.fragmentAdapter.StateFragmentActivity
import example.gesture.DispatchGestureActivity
import example.gesture.SimpleGestureActivity
import example.gesture.VerticalSlideFinishActivity
import example.viewPagerCards.fragments.CardFragmentActivity
import example.viewPagerCards.views.CardActivity

class GestureFragment : BaseNoPagerLazyRefreshFragment() {

    private lateinit var bd: FragmentHomeGestureBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bd = FragmentHomeGestureBinding.inflate(inflater, container, false)
        return bd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bd.refreshLayout.setEnablePureScrollMode(true)
        bd.viewStatusBarStyle.setOnClickListener { skip(EditLayoutChangeActivity::class.java) }
        bd.viewGestureDirection.setOnClickListener { skip(SimpleGestureActivity::class.java) }
        bd.viewSlideFinish.setOnClickListener { skip(DispatchGestureActivity::class.java) }
        bd.viewSlideBottomFinish.setOnClickListener { skip(VerticalSlideFinishActivity::class.java) }
        bd.viewDragRecyclerView.setOnClickListener { skip(DragActivity::class.java) }
        bd.btnSwipeMenu.setOnClickListener { skip(SwipeMenuActivity::class.java) }
        bd.btnCollapseModePin.setOnClickListener { skip(ScrollingActivity::class.java) }
        bd.btnCollapseModeTop.setOnClickListener { skip(ScrollingStopTopActivity::class.java) }
        bd.btnCollapseModeParallax.setOnClickListener { skip(ScrollingParallaxActivity::class.java) }
        bd.btnBottomSheetBehavior.setOnClickListener { skip(BottomSheetBehaviorActivity::class.java) }

        bd.btnViewPagerCardViews.setOnClickListener { skip(CardActivity::class.java) }
        bd.btnViewPagerCardFragments.setOnClickListener { skip(CardFragmentActivity::class.java) }
        bd.tabLayoutWithFragment.setOnClickListener{ skip(TabViewPager2Activity::class.java) }
        bd.viewPager2WithView.setOnClickListener{ skip(StateFragmentActivity::class.java) }

    }
}