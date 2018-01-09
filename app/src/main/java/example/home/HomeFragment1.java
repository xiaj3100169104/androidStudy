package example.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseFragment;
import com.style.framework.R;
import com.style.framework.databinding.FragmentHome1Binding;

import example.music.MusicListActivity;
import example.music.remote.RemotePlayActivity;
import example.softInput.SoftMode4Activity;
import example.activity.TestGestureActivity;
import example.activity.FileDownActivity;
import example.activity.MultiTypeActivity;
import example.activity.MyRadioGroupActivity;
import example.softInput.SoftMode1Activity;
import example.softInput.SoftMode2Activity;
import example.softInput.SoftMode3Activity;
import example.activity.ReadAssetsActivity;
import example.activity.WheelActivity;
import example.address.AddressActivity;
import example.album.SelectLocalPictureActivity;
import example.tablayout.TabLayoutActivity;
import example.viewpager.MyPickerActivity;


public class HomeFragment1 extends BaseFragment {

    FragmentHome1Binding bd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bd = DataBindingUtil.inflate(inflater, R.layout.fragment_home_1, container, false);
        return bd.getRoot();
    }

    @Override
    protected void initData() {
        bd.setOnItemClickListener(new OnItemClickListener());
    }

    public class OnItemClickListener {

        public void skip42(View v) {
            skip(MultiTypeActivity.class);
        }

        public void skip1(View v) {
            skip(SelectLocalPictureActivity.class);
        }

        public void skip2(View v) {
            skip(AddressActivity.class);
        }

        public void skip3(View v) {
            skip(MyRadioGroupActivity.class);
        }

        public void skip4(View v) {
            skip(WheelActivity.class);
        }

        public void skip5(View v) {
            skip(ReadAssetsActivity.class);
        }

        public void skip6(View v) {
            skip(FileDownActivity.class);
        }

        public void skip7(View v) {
            skip(SoftMode1Activity.class);
        }

        public void skip8(View v) {
            skip(SoftMode2Activity.class);
        }

        public void skip9(View v) {
            skip(SoftMode3Activity.class);
        }

        public void skip10(View v) {
            skip(TestGestureActivity.class);
        }

        public void skip11(View v) {
            skip(SoftMode4Activity.class);
        }

        public void skip12(View v) {
            skip(TabLayoutActivity.class);
        }

        public void skip13(View v) {
            skip(MyPickerActivity.class);
        }

        public void skip14(View v) {
            skip(MusicListActivity.class);
        }

        public void skip15(View v) {
            skip(RemotePlayActivity.class);
        }
    }
}