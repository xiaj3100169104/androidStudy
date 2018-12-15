package example.softInput;

import android.content.Intent;
import android.os.Bundle;

import com.style.base.BaseWhiteTitleBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.StatusbarStyleActivityMainBinding;

import org.jetbrains.annotations.Nullable;

public class StatusBarStyleMainActivity extends BaseWhiteTitleBarActivity {
    StatusbarStyleActivityMainBinding bd;

    @Override
    protected void onCreate(@Nullable Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.statusbar_style_activity_main);
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("全透明浅色标题栏");
        bd.viewCustomColor.setOnClickListener(v -> startActivity(new Intent(getContext(), MoveEditLayoutToTopActivity.class)));
        bd.viewThemeColor.setOnClickListener(v -> startActivity(new Intent(getContext(), SoftMode2Activity.class)));
        bd.viewTranslucentColor.setOnClickListener(v -> startActivity(new Intent(getContext(), HideStatusBarBottomEditLayoutActivity.class)));
        bd.viewTransparentColor.setOnClickListener(v -> startActivity(new Intent(getContext(), FullScreenBottomEditLayoutActivity.class)));
    }

}