package example.activity;

import android.os.Bundle
import com.style.base.BaseTitleBarActivity
import com.style.framework.R
import com.style.framework.databinding.ActivityUserAgreeBinding
import com.style.utils.AssetsUtil
import java.io.IOException

class ReadAssetsActivity : BaseTitleBarActivity() {

    private lateinit var bd: ActivityUserAgreeBinding

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        bd = ActivityUserAgreeBinding.inflate(layoutInflater)
        setContentView(bd.root)
        setTitleBarTitle("用户协议")
        readData()
    }

    private fun readData() {
        try {
            bd.tvUseragree.text = AssetsUtil.getAssetsText(this, "useragree.txt")
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
