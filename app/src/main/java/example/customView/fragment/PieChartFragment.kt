package example.customView.fragment

import androidx.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseFragment
import com.style.framework.R
import com.style.framework.databinding.ActivityPieChartBinding
import com.style.view.other.PieChartView
import java.util.*


class PieChartFragment : BaseFragment() {

    private lateinit var bd: ActivityPieChartBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.activity_pie_chart, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bd = DataBindingUtil.bind(view)!!
        bd.button.setOnClickListener { v ->
            bd.pieChart.setItems(getTestData2())
        }
        bd.button2.setOnClickListener { v ->
            bd.pieChart.setItemsWithAnimation(getTestData())
        }

    }

    fun getTestData(): List<PieChartView.PartItem> {
        val colors = intArrayOf(Color.parseColor("#FFA500"), Color.parseColor("#FFD700")
                , Color.parseColor("#BCEE68"), Color.parseColor("#008B00")
                , Color.parseColor("#00868B"), Color.parseColor("#1C86EE")
                , Color.parseColor("#CD2990"), Color.parseColor("#CD0000")
                , Color.parseColor("#CD4F39"))
        val percents = intArrayOf(46, 14, 8, 8, 8, 5, 5, 4, 2)
        val random = Random()
        /* percents[0] = random.nextInt(25) + 5
         percents[1] = random.nextInt(80 - percents[0]) + 5
         percents[2] = random.nextInt(80 - percents[0] - percents[1]) + 5
         percents[3] = 100 - percents[0] - percents[1] - percents[2]*/
        val list = ArrayList<PieChartView.PartItem>()
        var item: PieChartView.PartItem
        for (i in 0..8) {
            item = PieChartView.PartItem(colors[i], percents[i])
            list.add(item)
        }
        return list
    }

    fun getTestData2(): List<PieChartView.PartItem> {
        val colors = intArrayOf(Color.parseColor("#FF0000"), Color.parseColor("#0000FF"), Color.parseColor("#EE3B3B"), Color.parseColor("#696969"))
        val percents = intArrayOf(15, 25, 35, 25)
        val list = ArrayList<PieChartView.PartItem>()
        var item: PieChartView.PartItem
        for (i in 0..3) {
            item = PieChartView.PartItem(colors[i], percents[i])
            list.add(item)
        }
        return list
    }
}
