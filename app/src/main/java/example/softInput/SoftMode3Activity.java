package example.softInput;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode3Binding;


public class SoftMode3Activity extends BaseActivity {
    private String TAG = "SoftMode3Activity";

    ActivitySoftMode3Binding bd;

    @Override
    protected int getStatusBarStyle() {
        return STATUS_BAR_TRANSLUCENT;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_soft_mode_3;
    }

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("半透明状态栏下移动编辑框布局");
    }

}
