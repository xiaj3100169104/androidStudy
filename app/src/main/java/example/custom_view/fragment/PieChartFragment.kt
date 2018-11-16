package example.custom_view.fragment

import android.graphics.Color
import com.style.base.BaseFragment
import com.style.framework.R
import com.style.framework.databinding.ActivityPieChartBinding
import com.style.view.other.PieChartView
import java.util.*


class PieChartFragment : BaseFragment() {

    private lateinit var bd: ActivityPieChartBinding

    override fun getLayoutResId(): Int {
        return R.layout.activity_pie_chart
    }

    override fun initData() {
        bd = getBinding()
        bd.button.setOnClickListener { v ->
            bd.pieChart.setItems(getTestData())
        }
        bd.button2.setOnClickListener { v ->
            bd.pieChart.setItemsWithAnimation(getTestData())
        }

    }

    fun getTestData(): List<PieChartView.PartItem> {
        val colors = intArrayOf(Color.parseColor("#C0FF3E"), Color.parseColor("#FFF68F"), Color.parseColor("#FFDAB9"), Color.parseColor("#97FFFF"))
        val percents = intArrayOf(15, 25, 35, 25)
        val random = Random()
       /* percents[0] = random.nextInt(25) + 5
        percents[1] = random.nextInt(80 - percents[0]) + 5
        percents[2] = random.nextInt(80 - percents[0] - percents[1]) + 5
        percents[3] = 100 - percents[0] - percents[1] - percents[2]*/
        val list = ArrayList<PieChartView.PartItem>()
        var item: PieChartView.PartItem
        for (i in 0..3) {
            item = PieChartView.PartItem(colors[i], percents[i])
            list.add(item)
        }
        return list
    }
}
