package example.customview

import com.style.base.BaseActivity
import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import com.style.framework.databinding.ActivityHeartLineBinding
import com.style.view.HeartLineChart
import com.style.view.HeartRateLine
import java.io.FileReader

import java.util.ArrayList
import java.util.Random

class HeartLineActivity : BaseTitleBarActivity() {

    lateinit var bd: ActivityHeartLineBinding

    override fun getLayoutResId(): Int {
        return R.layout.activity_heart_line
    }

    override fun initData() {
        bd = getBinding()
        bd.btnRefresh.setOnClickListener { v -> refresh() }

    }

    fun refresh() {
        bd.heartLine.setData(heartData)
        bd.heartLineNew.setData(heartRateData)

    }

    private val heartRateData: ArrayList<HeartLineChart.HeartLineItem>
        get() {
            val list = ArrayList<HeartLineChart.HeartLineItem>()
            val random = Random()
            var item: HeartLineChart.HeartLineItem
            for (i in 0..1439) {
                val y = random.nextInt(60) + 60
                item = HeartLineChart.HeartLineItem(y, "00:00")
                list.add(item)
            }
            return list
        }

    private val heartData: ArrayList<HeartRateLine.PointItem>
        get() {
            val list = ArrayList<HeartRateLine.PointItem>()
            val random = Random()
            var item: HeartRateLine.PointItem
            for (i in 0..98) {
                val xLabel = "00:" + i.toString()
                val y: Int
                if (i > 2 && i < 5)
                    y = 0
                else if (i > 9 && i < 96 && i % 10 == 0)
                    y = 0
                else if (i > 150 && i < 200)
                    y = 0
                else if (i > 200 && i < 250 && i % 2 == 0)
                    y = 0
                else if (i == 998)
                    y = 0
                else
                    y = random.nextInt(60) + 50
                item = HeartRateLine.PointItem(xLabel, y)
                list.add(item)
            }
            return list
        }

}
