package example.customView

import android.os.Bundle
import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import com.style.framework.databinding.ActivityWeekSportBinding
import com.style.view.healthy.SportWeekHistogram
import java.util.*

class SportWeekActivity : BaseTitleBarActivity() {

    lateinit var bd: ActivityWeekSportBinding

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        bd = ActivityWeekSportBinding.inflate(layoutInflater)
        setContentView(bd.root)
        setTitleBarTitle("运动柱状图")
        bd.btnRefresh.setOnClickListener { v -> refresh() }
        bd.mSportWeek.setOnSelectionChangeListener(object : com.style.view.healthy.SportWeekHistogram.OnSelectionChangeListener {
            override fun onSelectionChanged(selected: Int) {
                //showToast(selected.toString())
                setStepsProgress(selected)
            }

            override fun onSlideLeft() {
                refresh()
            }

            override fun onSlideRight() {
                refresh()
            }
        })
    }

    fun refresh() {
        bd.mSportWeek.setData(getData(), true, 0)
        setStepsProgress(0)
    }

    private fun setStepsProgress(selected: Int) {
        if (weekSportData != null && selected >= 0 && selected <= weekSportData!!.size - 1) {
            val b = weekSportData!![selected]
            val progress: Int = (b.yValue / 5000f * bd.progressSteps.max).toInt()
            bd.progressSteps.setProgressWithAnimation(progress)
        }
    }

    private var weekSportData: ArrayList<com.style.view.healthy.SportWeekHistogram.PointItem>? = null

    private fun getData(): List<com.style.view.healthy.SportWeekHistogram.PointItem> {
        val list = ArrayList<com.style.view.healthy.SportWeekHistogram.PointItem>()
        var item: com.style.view.healthy.SportWeekHistogram.PointItem
        val random = Random()
        for (i in 0..6) {
            item = com.style.view.healthy.SportWeekHistogram.PointItem("", random.nextInt(3500) + 1000)
            list.add(item)
        }
        weekSportData = list
        return list
    }
}