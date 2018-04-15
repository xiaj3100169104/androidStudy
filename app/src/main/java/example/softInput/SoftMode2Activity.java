package example.softInput;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode2Binding;

public class SoftMode2Activity extends BaseToolBarActivity {

    ActivitySoftMode2Binding bd;


    @Override
    protected void customWindowOptions(Window w) {
        super.customWindowOptions(w);
       /* w.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        w.setStatusBarColor(Color.TRANSPARENT);*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_soft_mode_2);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("默认状态栏样式，适合白色标题栏的时候");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
