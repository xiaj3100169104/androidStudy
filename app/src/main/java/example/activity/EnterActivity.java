package example.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityEnterBinding;

/**
 * Created by xiajun on 2016/10/8.
 */
public class EnterActivity extends BaseToolBarActivity {

    ActivityEnterBinding bd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_enter);
        initData();
    }

    @Override
    public void initData() {
        super.customTitleOptions(bd.getRoot());
        setToolbarTitle("测试用例");
    }

}