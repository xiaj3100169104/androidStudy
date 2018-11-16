package example.custom_view

import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import com.style.framework.databinding.ActivityWeekSleepBinding
import com.style.view.healthy.SleepWeekHistogram
import java.util.*

class SleepWeekActivity : BaseTitleBarActivity() {

    lateinit var bd: ActivityWeekSleepBinding
    private var mWeeks: Array<String>? = null

    override fun getLayoutResId(): Int {
        return R.layout.activity_week_sleep
    }

    override fun initData() {
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
