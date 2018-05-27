package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.widget.TextView;

import com.style.base.BaseActivity;
import com.style.base.BaseActivityPresenter;
import com.style.framework.R;
import com.style.framework.databinding.ActivityUserAgreeBinding;
import com.style.utils.AssetsUtil;
import com.style.utils.StreamUtil;

import java.io.IOException;
import java.io.InputStream;

public class ReadAssetsActivity extends BaseActivity {

    ActivityUserAgreeBinding bd;
    private TextView tv_agree;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_user_agree;
    }

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    public void initData() {
        bd = getBinding();

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
