package example.customView.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseFragment
import com.style.framework.R
import com.style.framework.databinding.ActivityPieChartBinding
import java.util.*


class PieChartFragment : BaseFragment() {

    private lateinit var bd: ActivityPieChartBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_pie_chart, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bd = getBinding(view)
        bd.button.setOnClickListener { v ->
            bd.pieChart.setItems(getTestData())
        }
        bd.button2.setOnClickListener { v ->
            bd.pieChart.setItemsWithAnimation(getTestData())
        }

    }

    fun getTestData(): List<com.style.view.other.PieChartView.PartItem> {
        val colors = intArrayOf(Color.parseColor("#C0FF3E"), Color.parseColor("#FFF68F"), Color.parseColor("#FFDAB9"), Color.parseColor("#97FFFF"))
        val percents = intArrayOf(15, 25, 35, 25)
        val random = Random()
       /* percents[0] = random.nextInt(25) + 5
        percents[1] = random.nextInt(80 - percents[0]) + 5
        percents[2] = random.nextInt(80 - percents[0] - percents[1]) + 5
        percents[3] = 100 - percents[0] - percents[1] - percents[2]*/
        val list = ArrayList<com.style.view.other.PieChartView.PartItem>()
        var item: com.style.view.other.PieChartView.PartItem
        for (i in 0..3) {
            item = com.style.view.other.PieChartView.PartItem(colors[i], percents[i])
            list.add(item)
        }
        return list
    }
}
