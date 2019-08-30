package example.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseNoPagerLazyRefreshFragment
import com.style.framework.R
import example.activity.BottomSheetBehaviorActivity
import example.drag.*
import example.drag.scroll_stop.ScrollingStopTopActivity
import example.drag.swipeMenu.SwipeMenuActivity
import example.gesture.DispatchGestureActivity
import example.gesture.SimpleGestureActivity
import example.gesture.VerticalSlideFinishActivity
import example.editLayout.EditLayoutChangeActivity
import example.fragmentAdapter.IndexFragmentActivity
import example.fragmentAdapter.StateFragmentActivity
import example.viewPagerCards.fragments.CardFragmentActivity
import example.viewPagerCards.views.CardActivity
import kotlinx.android.synthetic.main.fragment_home_gesture.*

class GestureFragment : BaseNoPagerLazyRefreshFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_gesture, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        refreshLayout.setEnablePureScrollMode(true)
        view_status_bar_style.setOnClickListener { skip(EditLayoutChangeActivity::class.java) }
        view_gesture_direction.setOnClickListener { skip(SimpleGestureActivity::class.java) }
        view_slide_finish.setOnClickListener { skip(DispatchGestureActivity::class.java) }
        view_slide_bottom_finish.setOnClickListener { skip(VerticalSlideFinishActivity::class.java) }
        view_drag_recycler_view.setOnClickListener { skip(DragActivity::class.java) }
        btn_swipe_menu.setOnClickListener { skip(SwipeMenuActivity::class.java) }
        btn_collapseMode_pin.setOnClickListener { skip(ScrollingActivity::class.java) }
        btn_collapseMode_top.setOnClickListener { skip(ScrollingStopTopActivity::class.java) }
        btn_collapseMode_parallax.setOnClickListener { skip(ScrollingParallaxActivity::class.java) }
        btn_BottomSheetBehavior.setOnClickListener { skip(BottomSheetBehaviorActivity::class.java) }

        btn_view_pager_card_views.setOnClickListener { skip(CardActivity::class.java) }
        btn_view_pager_card_fragments.setOnClickListener { skip(CardFragmentActivity::class.java) }
        view_fragment_adapter.setOnClickListener{ skip(IndexFragmentActivity::class.java) }
        view_fragment_state_adapter.setOnClickListener{ skip(StateFragmentActivity::class.java) }

    }
}