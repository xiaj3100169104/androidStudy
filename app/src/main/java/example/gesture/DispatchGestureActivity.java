package example.gesture;

import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.GestureDivideScrollBinding;

/**
 * Created by xiajun on 2016/10/8.
 */
public class DispatchGestureActivity extends BaseActivity {

    GestureDivideScrollBinding bd;
    private boolean isDeleteExitAnim;

    @Override
    public int getLayoutResId() {
        return R.layout.gesture_divide_scroll;
    }

    @Override
    public void initData() {
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