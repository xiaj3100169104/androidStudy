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
public class EnterActivity extends BaseToolBarActivity implements View.OnClickListener {

    @Bind(R.id.btn_album)
    Button btnAlbum;
    @Bind(R.id.btn_address)
    Button btnAddress;
    @Bind(R.id.btn_RX)
    Button btnRX;
    @Bind(R.id.btn_event_bus)
    Button btnEventBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_enter;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {
        btnRX.setOnClickListener(this);
        btnAlbum.setOnClickListener(this);
        btnAddress.setOnClickListener(this);
        btnEventBus.setOnClickListener(this);
    }

    @OnClick(R.id.btn_eventbus_net)
    public void onClickbufferknife() {
        skip(TestNetActivity.class);
    }
    @Override
    public void onClick(View v) {
        Class<?> cls = null;
        switch (v.getId()) {
            case R.id.btn_RX:
                cls = TestNet2Activity.class;
                break;
            case R.id.btn_album:
                cls = SelectLocalPictureActivity.class;
                break;
            case R.id.btn_address:
                cls = AddressActivity.class;
                break;
            case R.id.btn_event_bus:
                cls = MainActivity.class;
                break;
        }
        if (cls != null)
            startActivity(new Intent(EnterActivity.this, cls));

    }
}