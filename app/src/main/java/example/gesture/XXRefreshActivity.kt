package example.gesture

import com.style.base.BaseDefaultTitleBarActivity

import com.style.framework.R
import com.style.framework.databinding.XxrefreshActivityBinding

class XXRefreshActivity : BaseDefaultTitleBarActivity() {

    lateinit var bd: XxrefreshActivityBinding

    override fun getLayoutResId(): Int {
        return R.layout.xxrefresh_activity
    }

    override fun initData() {
        bd = getBinding();

    }


}
