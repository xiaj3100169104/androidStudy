package example.customview

import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import com.style.framework.databinding.ActivityBpBinding
import com.style.view.healthy.BloodPressureLine
import java.util.*

class BpActivity : BaseTitleBarActivity() {

    lateinit var bd: ActivityBpBinding
    internal var max: Float = 0.toFloat()
    internal var min: Float = 0.toFloat()


    override fun getLayoutResId(): Int {
        return R.layout.activity_bp
    }

    override fun initData() {
        setToolbarTitle("血压曲线图")
        bd = getBinding()
        bd.btnRefresh.setOnClickListener { v -> refresh() }

    }

    fun refresh() {
        bd.bpLineBg.setData(getData())
        bd.bpLine.setData(getData())
    }

    private fun getData(): List<BloodPressureLine.BloodItem> {
        val list = ArrayList<BloodPressureLine.BloodItem>()
        val random = Random()
        for (i in 0..99) {
            val b = BloodPressureLine.BloodItem(random.nextInt(30) + 50, random.nextInt(30) + 90, "00:00")
            list.add(b)
        }
        return list
    }

}
