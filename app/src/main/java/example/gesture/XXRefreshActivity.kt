package example.gesture

import android.os.Bundle
import com.style.base.BaseTitleBarActivity

import com.style.framework.R
import com.style.framework.databinding.XxrefreshActivityBinding

class XXRefreshActivity : BaseTitleBarActivity() {

    lateinit var bd: XxrefreshActivityBinding

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        bd = XxrefreshActivityBinding.inflate(layoutInflater)
        setContentView(bd.root)

    }


}
