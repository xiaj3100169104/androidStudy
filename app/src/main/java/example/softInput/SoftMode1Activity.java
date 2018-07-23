package example.softInput;

import com.style.base.BaseActivity;
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
    protected int getStatusBarStyle() {
        return STATUS_BAR_COLOR;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("自定义状态栏颜色");
    }

}
