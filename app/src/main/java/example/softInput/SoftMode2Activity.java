package example.softInput;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode2Binding;

public class SoftMode2Activity extends BaseActivity {

    ActivitySoftMode2Binding bd;

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    protected int getStatusBarStyle() {
        return STATUS_BAR_THEME;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_soft_mode_2);
        super.setContentView(bd.getRoot());
    }

    @Override
    public void initData() {
        setToolbarTitle("状态栏为主题配置里的颜色");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
