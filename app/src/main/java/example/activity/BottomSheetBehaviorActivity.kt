package example.activity

import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.style.base.BaseTitleBarActivity
import com.style.framework.databinding.FragmentTablayoutBinding

class BottomSheetBehaviorActivity : BaseTitleBarActivity() {

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        var bd = FragmentTablayoutBinding.inflate(layoutInflater)
        setContentView(bd.root)
        val behavior = BottomSheetBehavior.from(bd.scroll)
        behavior.peekHeight = 50
        bd.tvContent.setOnClickListener {
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
