package example.gesture;

import android.os.Build;
import android.os.Bundle;

import com.style.base.BaseTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.GestureDivideScrollBinding;

import org.jetbrains.annotations.Nullable;

public class DispatchGestureActivity extends BaseTitleBarActivity {

    GestureDivideScrollBinding bd;
    private boolean isDeleteExitAnim;

    @Override
    public void setRequestedOrientation(int requestedOrientation) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O)
            return;
        super.setRequestedOrientation(requestedOrientation);
    }

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        bd = GestureDivideScrollBinding.inflate(getLayoutInflater());
        setContentView(bd.getRoot());

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