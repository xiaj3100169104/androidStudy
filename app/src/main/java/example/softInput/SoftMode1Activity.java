package example.softInput;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode1Binding;

//
public class SoftMode1Activity extends BaseToolBarActivity {

    ActivitySoftMode1Binding bd;

    @Override
    protected boolean isFitSystemWindows() {
        return true;
    }

    @Override
    protected boolean isFlagTranslucentStatus() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_soft_mode_1);
        super.setContentView(bd.getRoot());

        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("状态栏显示为主题配置里的颜色");
    }

}
