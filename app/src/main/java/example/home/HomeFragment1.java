package example.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseFragment;
import com.style.framework.R;

import butterknife.OnClick;
import example.activity.FileDownActivity;
import example.activity.MultiTypeActivity;
import example.activity.MyRadioGroupActivity;
import example.activity.SoftMode1Activity;
import example.activity.SoftMode2Activity;
import example.activity.SoftMode3Activity;
import example.activity.ReadAssetsActivity;
import example.activity.WheelActivity;
import example.address.AddressActivity;
import example.album.SelectLocalPictureActivity;


public class HomeFragment1 extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_home_1;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
    }

    @OnClick(R.id.layout_item_42)
    public void skip42() {
        skip(MultiTypeActivity.class);
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
        skip(ReadAssetsActivity.class);
    }

    @OnClick(R.id.btn_file_down)
    public void skip6() {
        skip(FileDownActivity.class);
    }
    @OnClick(R.id.btn_1)
    public void skip7() {
        skip(SoftMode1Activity.class);
    }
    @OnClick(R.id.btn_2)
    public void skip8() {
        skip(SoftMode2Activity.class);
    }
    @OnClick(R.id.btn_3)
    public void skip9() {
        skip(SoftMode3Activity.class);
    }
}