package example.gesture;

import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.GestureDivideScrollBinding;

/**
 * Created by xiajun on 2016/10/8.
 */
public class DispatchGestureActivity extends BaseActivity {

    GestureDivideScrollBinding bd;

    @Override
    public int getLayoutResId() {
        return R.layout.gesture_divide_scroll;
    }

    @Override
    public void initData() {
        bd = getBinding();
        bd.tvContent1.setOnClickListener(v -> logE(TAG, "111111111111"));
        bd.tv2.setOnClickListener(v -> logE(TAG, "22222222222222"));
    }

}