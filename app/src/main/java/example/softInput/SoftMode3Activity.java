package example.softInput;

import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode3Binding;

import example.gesture.BaseRightSlideFinishActivity;


public class SoftMode3Activity extends BaseRightSlideFinishActivity {
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
    public void initData() {
        bd = getBinding();
        setToolbarTitle("半透明状态栏下移动编辑框布局");
    }

}
