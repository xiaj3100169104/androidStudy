package example.softInput;

import android.os.Bundle;

import com.style.base.BaseHideStatusTitleBarActivity;
import com.style.base.BaseWhiteTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode3Binding;

import org.jetbrains.annotations.Nullable;

import example.gesture.BaseRightSlideFinishActivity;


public class HideStatusBarBottomEditLayoutActivity extends BaseHideStatusTitleBarActivity {
    private String TAG = "SoftMode3Activity";

    ActivitySoftMode3Binding bd;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_soft_mode_3);
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("调整编辑布局");
    }

}
