package example.softInput;

import android.os.Bundle;

import com.style.base.BaseDefaultTitleBarActivity;
import com.style.base.BaseHideStatusTitleBarActivity;
import com.style.base.BaseWhiteTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode1Binding;

import org.jetbrains.annotations.Nullable;

import example.gesture.BaseRightSlideFinishActivity;

public class MoveEditLayoutToTopActivity extends BaseHideStatusTitleBarActivity {

    ActivitySoftMode1Binding bd;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.activity_soft_mode_1);
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("整体上移");
    }

}
