package example.editLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.style.base.activity.BaseFullScreenStableTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.EditLayoutMainActivityBinding;
import com.style.utils.DeviceInfoUtil;

import org.jetbrains.annotations.Nullable;

public class EditLayoutChangeActivity extends BaseFullScreenStableTitleBarActivity {
    EditLayoutMainActivityBinding bd;

    @Override
    public boolean isLightStatusBar() {
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.edit_layout_main_activity);
        bd = getBinding();
        setToolbarTitle("编辑布局调整");
        bd.viewCustomColor.setOnClickListener(v -> {
            startActivity(new Intent(getContext(), MoveEditLayoutToTopActivity.class));
        });
        bd.viewTranslucentColor.setOnClickListener(v -> startActivity(new Intent(getContext(), HideStatusBarBottomEditLayoutActivity.class)));
        bd.viewTransparentColor.setOnClickListener(v -> startActivity(new Intent(getContext(), FullScreenBottomEditLayoutActivity.class)));
        bd.viewB.setOnClickListener(v -> {
            int h = DeviceInfoUtil.getScreenHeight(this);
            int h0 = getStatusHeight();
            int y1 = getLocation(bd.viewTop);
            int y2 = getLocation(bd.viewB);
            int h1 = bd.viewB.getHeight();
            int y3 = h - (y2 + h1 - h0);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) bd.viewBottom.getLayoutParams();
            lp.height = y3;
            bd.viewBottom.setLayoutParams(lp);
        });
        bd.viewBottom.getLayoutParams().height = 139;

    }

    /**
     * 计算控件的坐标 以便顶部悬浮
     *
     * @param v
     * @return
     */
    public int getLocation(View v) {
        int[] location = new int[2];
        v.getLocationOnScreen(location);
        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(w, h);
        return location[1];
    }
}