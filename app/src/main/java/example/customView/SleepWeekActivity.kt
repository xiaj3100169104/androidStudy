package example.customView

import android.os.Bundle
import com.style.base.BaseDefaultTitleBarActivity
import com.style.framework.R
import com.style.framework.databinding.ActivityWeekSleepBinding
import com.style.view.healthy.SleepWeekHistogram
import java.util.*

class SleepWeekActivity : BaseDefaultTitleBarActivity() {

    lateinit var bd: ActivityWeekSleepBinding
    private var mWeeks: Array<String>? = null

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.activity_week_sleep)
        setToolbarTitle("睡眠柱状图")
        bd = getBinding()
        bd.btnRefresh.setOnClickListener { v -> refresh() }
        mWeeks = resources.getStringArray(R.array.week_array)
        bd.sleepHistogram.setOnSelectionChangeListener(object : SleepWeekHistogram.OnSelectionChangeListener {
            override fun onSelectionChanged(selected: Int) {
                showToast(selected.toString())
            }

            override fun onSlideLeft() {
                refresh()
            }

            override fun onSlideRight() {
                refresh()
            }
        })
        bd.sleepHistogram.setData(null, false)
    }

    fun refresh() {
        bd.sleepHistogram.setData(getData(), true)
    }

    fun getData(): List<SleepWeekHistogram.PointItem> {
        val list = ArrayList<SleepWeekHistogram.PointItem>()
        var item: SleepWeekHistogram.PointItem
        val random = Random()
        for (i in 0..6) {
            item = SleepWeekHistogram.PointItem("", random.nextInt(500) + 60)
            list.add(item)
        }
        return list
    }

}
