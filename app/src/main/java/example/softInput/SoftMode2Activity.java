package example.softInput;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode2Binding;
import com.style.helper.EditTextHelper;

public class SoftMode2Activity extends BaseActivity {

    ActivitySoftMode2Binding bd;

    @Override
    protected int getStatusBarStyle() {
        return STATUS_BAR_THEME;
    }

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
}
