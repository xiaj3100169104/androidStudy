package example.customview

import com.style.base.BaseActivity
import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import com.style.framework.databinding.ActivityTempBinding
import com.style.view.TemperatureChart
import com.style.view.TemperatureLineNew

import java.util.ArrayList
import java.util.Random

class TempActivity : BaseTitleBarActivity() {

    lateinit var bd: ActivityTempBinding
    internal var max: Float = 0.toFloat()
    internal var min: Float = 0.toFloat()


    override fun getLayoutResId(): Int {
        return R.layout.activity_temp
    }

    override fun initData() {
        bd = getBinding()
        bd.btnRefresh.setOnClickListener { v -> refresh() }

    }

    fun refresh() {
        bd.temperatureLine.setData(temperatureData)
        bd.temperatureChart.setData(data)
    }

    private val data: List<TemperatureChart.TempItem>
        get() {
            val list = ArrayList<TemperatureChart.TempItem>()
            val random = Random()
            for (i in 0..199) {
                val b = TemperatureChart.TempItem(random.nextFloat() + 36.5f, i.toString())
                list.add(b)
            }
            return list
        }

    private// + String.valueOf(i);
    //Log.e("" + i, y + "");
    val temperatureData: ArrayList<TemperatureLineNew.PointItem>
        get() {

            val list = ArrayList<TemperatureLineNew.PointItem>()
            val random = Random()
            var item: TemperatureLineNew.PointItem

            for (i in 0..1499) {
                val xLabel = "00:00"

                val y: Float
                if (i > 2 && i < 8)
                    y = 0f
                else if (i > 9 && i < 15)
                    y = 0f
                else if (i > 20 && i < 30)
                    y = 0f
                else if (i > 35 && i < 50 && i % 2 == 0)
                    y = 0f
                else if (i <= 1498 && i >= 1495)
                    y = 0f
                else
                    y = 35f + random.nextInt(3).toFloat() + random.nextFloat()

                if (i == 0) {
                    min = y
                    max = min
                }
                if (y > max)
                    max = y
                if (y < min && y != 0f)
                    min = y
                item = TemperatureLineNew.PointItem(xLabel, y)

                list.add(item)
            }
            bd.temperatureLineBg.setMaxAndMin(max, min)
            bd.temperatureLineBg.setAverageValue(36.3f)
            bd.temperatureLine.setMaxAndMin(max, min)
            return list
        }
}
