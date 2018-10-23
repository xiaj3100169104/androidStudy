package example.customview

import android.util.Log
import com.style.base.BaseActivity
import com.style.base.BaseTitleBarActivity

import com.style.framework.R
import com.style.framework.databinding.ActivityCurveBinding
import com.style.view.SleepWeekHistogram

import java.util.ArrayList
import java.util.Random

class SleepWeekActivity : BaseTitleBarActivity() {

    lateinit var bd: ActivityCurveBinding
    private var mWeeks: Array<String>? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_curve
    }

    override fun initData() {
        bd = getBinding()
        bd.btnRefresh.setOnClickListener { v -> refresh() }

        mWeeks = resources.getStringArray(R.array.week_array)
        bd.sleepHistogram.setOnSelectionChangeListener { selected -> Log.e("CurveActivity2", selected.toString() + "") }
        bd.sleepHistogram.setData(null, false)
    }

    fun refresh() {
        bd.sleepHistogram.setData(histogramData, true)
    }

    val histogramData: List<SleepWeekHistogram.PointItem>
        get() {
            val mValueList = ArrayList<SleepWeekHistogram.PointItem>()
            val random = Random()
            var y: Int
            var item: SleepWeekHistogram.PointItem
            for (i in 0..6) {
                y = random.nextInt(180) + 360
                item = SleepWeekHistogram.PointItem(mWeeks!![i], y)
                mValueList.add(item)
            }
            return mValueList
        }

}
