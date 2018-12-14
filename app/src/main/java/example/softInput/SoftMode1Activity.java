package example.softInput;

import com.style.base.BaseWhiteTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode1Binding;

import example.gesture.BaseRightSlideFinishActivity;

public class SoftMode1Activity extends BaseWhiteTitleBarActivity {

    ActivitySoftMode1Binding bd;
    @Override
    public int getLayoutResId() {
        return R.layout.activity_soft_mode_1;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("自定义状态栏颜色");
    }

}
