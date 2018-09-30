package example.activity;

import android.widget.TextView;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityUserAgreeBinding;
import com.style.utils.AssetsUtil;
import kotlinx.android.synthetic.main.activity_user_agree.*
import java.io.IOException

public class ReadAssetsActivity : BaseTitleBarActivity() {

    lateinit var bd: ActivityUserAgreeBinding;

    override fun getLayoutResId(): Int {
        return R.layout.activity_user_agree;
    }

    override fun initData() {
        bd = getBinding();
        setToolbarTitle("用户协议");
        readData();
    }

    fun readData() {
        try {

            tv_useragree.setText(AssetsUtil.getAssetsText(this, "useragree.txt"));
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
