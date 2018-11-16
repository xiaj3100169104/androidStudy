package example.softInput;

import android.content.Intent;
import android.view.View;

import com.style.framework.R;
import com.style.framework.databinding.StatusbarStyleActivityMainBinding;

import example.gesture.BaseRightSlideFinishActivity;

/**
 * Created by xiajun on 2016/10/8.
 */
public class StatusBarStyleMainActivity extends BaseRightSlideFinishActivity {
    StatusbarStyleActivityMainBinding bd;

    @Override
    public boolean isLightStatusBar() {
        return true;
    }

    @Override
    public int getLayoutResId() {
        return R.layout.statusbar_style_activity_main;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("全透明浅色标题栏");
        bd.viewCustomColor.setOnClickListener(v -> startActivity(new Intent(getContext(), SoftMode1Activity.class)));
        bd.viewThemeColor.setOnClickListener(v -> startActivity(new Intent(getContext(), SoftMode2Activity.class)));
        bd.viewTranslucentColor.setOnClickListener(v -> startActivity(new Intent(getContext(), SoftMode3Activity.class)));
        bd.viewTransparentColor.setOnClickListener(v -> startActivity(new Intent(getContext(), SoftMode4Activity.class)));
    }

}