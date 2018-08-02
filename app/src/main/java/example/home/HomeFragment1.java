package example.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.style.base.BaseFragment;
import com.style.framework.R;
import com.style.framework.databinding.FragmentHome1Binding;

import example.activity.MyRadioGroupActivity;
import example.customview.activity.CustomViewMainActivity;
import example.gesture.DispatchGestureActivity;
import example.gesture.SimpleGestureActivity;
import example.gesture.TestGestureActivity;
import example.dialog.WheelActivity;
import example.address.AddressActivity;
import example.album.SelectLocalPictureActivity;
import example.dialog.DialogActivity;
import example.drag.DragActivity;
import example.filedown.FileDownActivity;
import example.music.MusicListActivity;
import example.music.remote.RemotePlayActivity;
import example.softInput.StatusBarStyleMainActivity;
import example.tablayout.TabLayoutActivity;
import example.vlayout.MultiTypeActivity;
import fussen.cc.barchart.activity.CustomScrollMainActivity;


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
        bd.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        bd.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000/*,false*/);//传入false表示加载失败
            }
        });
    }

    public class OnItemClickListener {
        public void skip01(View v) {
            skip(CustomViewMainActivity.class);
        }

        public void skip02(View v) {
            skip(CustomScrollMainActivity.class);
        }

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

        public void skip31(View v) {
            skip(DialogActivity.class);
        }

        public void skip4(View v) {
            skip(WheelActivity.class);
        }

        public void skip6(View v) {
            skip(FileDownActivity.class);
        }

        public void skip9(View v) {
            skip(SimpleGestureActivity.class);
        }

        public void skip91(View v) {
            skip(DispatchGestureActivity.class);
        }

        public void skip10(View v) {
            skip(TestGestureActivity.class);
        }

        public void skip11(View v) {
            skip(StatusBarStyleMainActivity.class);
        }

        public void skip12(View v) {
            skip(TabLayoutActivity.class);
        }

        public void skip13(View v) {
            skip(DragActivity.class);
        }

        public void skip14(View v) {
            skip(MusicListActivity.class);
        }

        public void skip15(View v) {

            skip(RemotePlayActivity.class);

        }
    }
}