package example.softInput;

import android.content.Intent;
import android.view.View;

import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.StatusbarStyleActivityMainBinding;

import example.gesture.BaseLeftSlideFinishActivity;

/**
 * Created by xiajun on 2016/10/8.
 */
public class StatusBarStyleMainActivity extends BaseLeftSlideFinishActivity {
    StatusbarStyleActivityMainBinding bd;

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
        bd.setOnItemClickListener(new OnItemClickListener());
    }

    public class OnItemClickListener {

        public void skip1(View v) {
            startActivity(new Intent(getContext(), SoftMode1Activity.class));
        }

        public void skip2(View v) {
            startActivity(new Intent(getContext(), SoftMode2Activity.class));
        }

        public void skip3(View v) {
            startActivity(new Intent(getContext(), SoftMode3Activity.class));
        }

        public void skip4(View v) {
            startActivity(new Intent(getContext(), SoftMode4Activity.class));

        }
    }
}