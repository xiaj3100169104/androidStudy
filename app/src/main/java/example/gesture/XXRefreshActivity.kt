package example.gesture

import android.text.InputFilter
import android.util.Log
import com.style.base.BaseActivity
import com.style.base.BaseTitleBarActivity

import com.style.framework.R
import com.style.framework.databinding.ActivityCurveBinding
import com.style.framework.databinding.ActivityExpandTextBinding
import com.style.view.SleepWeekHistogram

import java.util.ArrayList
import java.util.Random

class XXRefreshActivity : BaseTitleBarActivity() {

    lateinit var bd: ActivityExpandTextBinding

    override fun getLayoutResId(): Int {
        return R.layout.activity_expand_text
    }

    override fun initData() {
        bd = getBinding();

    }


}
