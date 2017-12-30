package example.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseFragment;
import com.style.framework.R;
import com.style.framework.databinding.FragmentHome1Binding;

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
    }
}