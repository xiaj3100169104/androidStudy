package example.softInput;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode1Binding;

//
public class SoftMode1Activity extends BaseActivity {

    ActivitySoftMode1Binding bd;

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    protected int getStatusBarStyle() {
        return STATUS_BAR_DEFAULT;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_soft_mode_1);
        super.setContentView(bd.getRoot());
    }

    @Override
    public void initData() {
        setToolbarTitle("默认状态栏样式");
    }

}
