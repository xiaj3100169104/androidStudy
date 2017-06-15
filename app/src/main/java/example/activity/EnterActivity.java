package example.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.style.address.AddressActivity;
import com.style.album.SelectLocalPictureActivity;
import com.style.base.BaseToolBarActivity;
import com.style.framework.R;

import butterknife.Bind;
import butterknife.OnClick;

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