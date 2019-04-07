package example.gesture;

import android.os.Bundle;

import com.style.base.activity.BaseDefaultTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.GestureDivideScrollBinding;

import org.jetbrains.annotations.Nullable;

public class DispatchGestureActivity extends BaseDefaultTitleBarActivity {

    GestureDivideScrollBinding bd;
    private boolean isDeleteExitAnim;

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        return;
    }

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.gesture_divide_scroll);
        bd = getBinding();
        bd.tvContent1.setOnClickListener(v -> logE(getTAG(), "111111111111"));
        bd.tv2.setOnClickListener(v -> logE(getTAG(), "22222222222222"));
        bd.root.setOnSlideListener(() -> {
            isDeleteExitAnim = true;
            finish();
        });
    }

    @Override
    protected void onPause() {
        if (isDeleteExitAnim)
            overridePendingTransition(0, 0);
        super.onPause();
    }
}