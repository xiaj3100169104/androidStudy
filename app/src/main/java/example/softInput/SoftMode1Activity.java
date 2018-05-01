package example.softInput;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode1Binding;

//
public class SoftMode1Activity extends BaseActivity {

    ActivitySoftMode1Binding bd;
    protected boolean isTransparentStatusBar() {
        return false;
    }
    @Override
    protected boolean isDefaultStatusBar() {
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_soft_mode_1);
        super.setContentView(bd.getRoot());
    }

    @Override
    public void initData() {
        setToolbarTitle("状态栏显示为主题配置里的颜色");
    }

}
