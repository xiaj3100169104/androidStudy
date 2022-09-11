package example.customView

import android.os.Bundle
import com.style.base.BaseTitleBarActivity
import com.style.framework.databinding.ActivityWeekSleepBinding
import java.text.SimpleDateFormat
import java.util.*

class SleepWeekActivity : BaseTitleBarActivity() {

    private lateinit var bd: ActivityWeekSleepBinding

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        bd = ActivityWeekSleepBinding.inflate(layoutInflater)
        setContentView(bd.root)
        setTitleBarTitle("睡眠图")
        bd.btnRefresh.setOnClickListener { v -> refresh() }
        bd.sleepHistogram.setOnSelectionChangeListener(object : com.style.view.healthy.SleepWeekHistogram.OnSelectionChangeListener {
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
        bd.sleepDetailView.setData(getSleepDetail())
    }

    fun getData(): List<com.style.view.healthy.SleepWeekHistogram.PointItem> {
        val list = ArrayList<com.style.view.healthy.SleepWeekHistogram.PointItem>()
        var item: com.style.view.healthy.SleepWeekHistogram.PointItem
        val random = Random()
        for (i in 0..6) {
            item = com.style.view.healthy.SleepWeekHistogram.PointItem("", random.nextInt(500) + 60)
            list.add(item)
        }
        return list
    }

    private fun getSleepDetail(): com.style.view.healthy.SleepDetailView.DetailBean? {
        val data = com.style.view.healthy.SleepDetailView.DetailBean()
        data.totalSleepTimeLength = 60 * 60 * 9
        data.xLabel = arrayOfNulls<String>(com.style.view.healthy.SleepDetailView.DetailBean.X_LABEL_COUNT)
        val mHourFormat = SimpleDateFormat("HH:mm")
        val interval = data.totalSleepTimeLength / 6
        for (i in 0..6) {
            data.xLabel[i] = i.toString()
        }
        //睡眠类型
        data.mSleepTime = IntArray(com.style.view.healthy.SleepDetailView.DetailBean.SLEEP_TYPE_COUNT)
        data.mSleepTime[0] = data.totalSleepTimeLength /60 / 4
        data.mSleepTime[1] = data.totalSleepTimeLength /60 / 4
        data.mSleepTime[2] = data.totalSleepTimeLength /60 / 4
        data.mSleepTime[3] = data.totalSleepTimeLength /60 / 4

        //睡眠时间段列表
        val itemCount = 10
        val mItemList = ArrayList<com.style.view.healthy.SleepDetailView.SleepItem>()
        var item: com.style.view.healthy.SleepDetailView.SleepItem
        for (i in 0 until itemCount) {
            item = com.style.view.healthy.SleepDetailView.SleepItem()
            item.phaseType = i % 4
            item.phaseLength = data.totalSleepTimeLength / 10
            item.mStartTime = i.toLong()
            item.mEndTime = i.toLong()
            mItemList.add(item)
        }
        mItemList.sortBy { it.mStartTime }
        data.sleepItems = mItemList
        return data
    }
}
