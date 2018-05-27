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
    public int getLayoutResId() {
        return R.layout.activity_soft_mode_1;
    }
    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    protected int getStatusBarStyle() {
        return STATUS_BAR_DEFAULT;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("默认状态栏样式");
    }

}
