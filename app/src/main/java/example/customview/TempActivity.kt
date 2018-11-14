package example.customview

import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import com.style.framework.databinding.ActivityTempBinding
import com.style.view.healthy.TemperatureLineNew
import java.util.*

class TempActivity : BaseTitleBarActivity() {

    lateinit var bd: ActivityTempBinding
    internal var max: Float = 36.5.toFloat()
    internal var min: Float = 36.5.toFloat()


    override fun getLayoutResId(): Int {
        return R.layout.activity_temp
    }

    override fun initData() {
        setToolbarTitle("体温波动图")
        bd = getBinding()
        bd.btnRefresh.setOnClickListener { v -> refresh() }

    }

    fun refresh() {
        var list = getData()
        bd.temperatureLineBg.setMaxAndMin(max, min)
        //bd.temperatureLineBg.averageValue(bean.averageValue);
        bd.temperatureLine.setMaxAndMin(max, min)
        bd.temperatureLine.setData(list)
    }

    private fun getData(): List<TemperatureLineNew.PointItem> {
        val list = ArrayList<TemperatureLineNew.PointItem>()
        val random = Random()
        for (i in 0..200) {
            val y: Float = String.format("%.1f", random.nextFloat()).toFloat() + 36.5f
            val b = TemperatureLineNew.PointItem("00:00", y)
            list.add(b)
            if (y > max)
                max = y
            if (y < min)
                min = y
        }
        return list
    }
}
