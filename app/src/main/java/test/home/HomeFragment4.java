package test.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseFragment;
import com.style.db.custom.TestCustomDBActivity;
import com.style.framework.R;

import butterknife.OnClick;
import test.activity.EnterActivity;


public class HomeFragment4 extends BaseFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_home_4;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onLazyLoad() {

    }
    @OnClick(R.id.layout_item_47)
    public void skip47() {
        skip(TestCustomDBActivity.class);
    }
    @OnClick(R.id.layout_item_48)
    public void skip48() {
        skip(EnterActivity.class);
    }
}
