package example.customview

import com.style.base.BaseActivity
import com.style.base.BaseFragment
import com.style.framework.R
import com.style.framework.databinding.ActivityBpBinding
import com.style.view.BloodPressureChart

import java.util.ArrayList
import java.util.Random

class BpActivity : BaseActivity() {

    lateinit var bd: ActivityBpBinding
    internal var max: Float = 0.toFloat()
    internal var min: Float = 0.toFloat()


    override fun getLayoutResId(): Int {
        return R.layout.activity_bp
    }
    override fun isGeneralTitleBar(): Boolean {
        return false
    }
    override fun initData() {
        bd = getBinding()
        bd.btnRefresh.setOnClickListener { v -> refresh() }

    }

    fun refresh() {
        bd.bpLine.setData(getData())
    }

    private fun getData(): List<BloodPressureChart.BloodItem> {
        val list = ArrayList<BloodPressureChart.BloodItem>()
        val random = Random()
        for (i in 0..99) {
            val b = BloodPressureChart.BloodItem(random.nextInt(30) + 50, random.nextInt(30) + 90, i.toString())
            list.add(b)
        }
        return list
    }
}
