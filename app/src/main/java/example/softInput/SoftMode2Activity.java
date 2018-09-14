package example.softInput;

import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode2Binding;

import example.gesture.BaseRightSlideFinishActivity;

public class SoftMode2Activity extends BaseRightSlideFinishActivity {

    ActivitySoftMode2Binding bd;

    @Override
    public int getStatusBarStyle() {
        return STATUS_BAR_THEME;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.activity_soft_mode_2;
    }


    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("状态栏为主题配置里的颜色");
    }
}
