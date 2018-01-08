package example.softInput;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivitySoftMode1Binding;
import com.style.framework.databinding.ActivitySoftMode3Binding;

//
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
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus){
            bd.content.setPadding(0, 0, 0, 0);

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) bd.content.getLayoutParams();
        logE(TAG, "topMargin==" + lp.topMargin + "  toppaddding==" + bd.content.getPaddingTop());
    }
}
