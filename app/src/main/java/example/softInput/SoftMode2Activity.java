package example.softInput;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode2Binding;

public class SoftMode2Activity extends BaseActivity {

    ActivitySoftMode2Binding bd;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_soft_mode_2;
    }

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }
    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("状态栏为主题配置里的颜色");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
