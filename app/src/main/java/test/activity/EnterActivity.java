package test.activity;

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

    @OnClick(R.id.btn_album)
    public void skip1() {
        skip(SelectLocalPictureActivity.class);
    }
    @OnClick(R.id.btn_address)
    public void skip2() {
        skip(AddressActivity.class);
    }
    @OnClick(R.id.btn_radio)
    public void skip3() {
        skip(MyRadioGroupActivity.class);
    }
    @OnClick(R.id.btn_wheel)
    public void skip4() {
        skip(WheelActivity.class);
    }
    @OnClick(R.id.btn_user_agree)
    public void skip5() {
        skip(UserAgreeActivity.class);
    }
    @OnClick(R.id.btn_web_view)
    public void skip6() {
        skip(WebViewActivity.class);
    }
    @OnClick(R.id.btn_file_down)
    public void skip7() {
        skip(FileDownActivity.class);
    }
}