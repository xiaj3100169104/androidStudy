package example.activity

import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.util.Log
import android.view.View
import com.style.base.activity.BaseTitleBarActivity
import com.style.framework.R
import kotlinx.android.synthetic.main.fragment_tablayout.*

class BottomSheetBehaviorActivity : BaseTitleBarActivity() {

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.fragment_tablayout)
        val behavior = BottomSheetBehavior.from(scroll)
        behavior.peekHeight = 50
        tv_content.setOnClickListener {
            if (behavior.state == BottomSheetBehavior.STATE_EXPANDED) {
                behavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
            } else {
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED)
            }
        }
        behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                Log.e(TAG, "onStateChanged  $newState")

            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                Log.e(TAG, "onSlide  $slideOffset")

            }
        })
    }
}
