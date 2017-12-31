package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode1Binding;
import com.style.framework.databinding.ActivitySoftMode3Binding;

public class SoftMode1Activity extends BaseToolBarActivity {

    ActivitySoftMode1Binding bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_soft_mode_1);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("弹出软键盘缩减布局高度");
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
