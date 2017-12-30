package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityUserAgreeBinding;
import com.style.utils.AssetsUtil;
import com.style.utils.StreamUtil;

import java.io.IOException;
import java.io.InputStream;

public class ReadAssetsActivity extends BaseToolBarActivity {

    ActivityUserAgreeBinding bd;
    private TextView tv_agree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_user_agree);
        initData();
    }

    @Override
    public void initData() {
        super.customTitleOptions(bd.getRoot());

        setToolbarTitle("用户协议");

        tv_agree = (TextView) findViewById(R.id.tv_useragree);
        readData();
    }

    public void readData() {
        try {

            tv_agree.setText(AssetsUtil.getAssetsText(this, "useragree.txt"));
        } catch (IOException e) {

        }
    }
}
