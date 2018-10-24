package example.gesture

import com.style.base.BaseTitleBarActivity

import com.style.framework.R
import com.style.framework.databinding.XxrefreshActivityBinding

class XXRefreshActivity : BaseTitleBarActivity() {

    lateinit var bd: XxrefreshActivityBinding

    override fun getLayoutResId(): Int {
        return R.layout.xxrefresh_activity
    }

    override fun initData() {
        bd = getBinding();

    }


}
