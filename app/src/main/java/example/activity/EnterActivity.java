package example.activity;

import android.os.Bundle;

import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

/**
 * Created by xiajun on 2016/10/8.
 */
public class EnterActivity extends BaseToolBarActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_enter;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        setToolbarTitle("测试用例");
    }

}