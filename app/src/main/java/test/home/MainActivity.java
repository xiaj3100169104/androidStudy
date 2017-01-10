package test.home;

import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.broadcast.NetWorkChangeBroadcastReceiver;
import com.style.framework.R;

import butterknife.Bind;

public class MainActivity extends BaseToolBarActivity {

    @Bind(R.id.layout_bottom)
    RelativeLayout layoutBottom;
    @Bind(R.id.view_home_tap_1)
    TextView viewHomeTap1;
    @Bind(R.id.layout_home_tap_1)
    RelativeLayout layoutHomeTap1;
    @Bind(R.id.view_home_tap_2)
    TextView viewHomeTap2;
    @Bind(R.id.layout_home_tap_2)
    RelativeLayout layoutHomeTap2;
    @Bind(R.id.view_home_tap_3)
    TextView viewHomeTap3;
    @Bind(R.id.layout_home_tap_3)
    RelativeLayout layoutHomeTap3;
    @Bind(R.id.view_home_tap_4)
    TextView viewHomeTap4;
    @Bind(R.id.layout_home_tap_4)
    RelativeLayout layoutHomeTap4;
    protected static final String[] fragTags = {"tag1", "tag2", "tag3", "tag4", "tag5"};
    protected static final String[] titles = {"沟通", "首页", "工单", "通讯录", "客户"};

    private HomeFragment1 homeFragment1;
    private HomeFragment2 homeFragment2;
    private HomeFragment3 homeFragment3;
    private HomeFragment4 homeFragment4;
    private FragmentManager fm;
    private FragmentTransaction bt;
    private TextView[] mTabs;
    public int currentTabIndex = 0;
    public Fragment[] fragments;
    private User curUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_main;
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initData() {

        mTabs = new TextView[5];
        mTabs[0] = viewHomeTap1;
        mTabs[1] = viewHomeTap2;
        mTabs[2] = viewHomeTap3;
        mTabs[3] = viewHomeTap4;
        homeFragment1 = new HomeFragment1();
        homeFragment2 = new HomeFragment2();
        homeFragment3 = new HomeFragment3();
        homeFragment4 = new HomeFragment4();

        fragments = new Fragment[]{homeFragment1, homeFragment2, homeFragment3, homeFragment4};
        fm = getSupportFragmentManager();
        bt = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            bt.add(R.id.content, fragments[i], fragTags[i]);
        }
        bt.commit();

        /*int num = fragments.length;
        pgAdapyer = new MyAdapter(getSupportFragmentManager());
        pager.setAdapter(pgAdapyer);
        pager.setOffscreenPageLimit(num);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentTabIndex = position;
                showSelectedTab();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/
        showSelectedTab();
    }

    private void showSelectedTab() {
        bt = fm.beginTransaction();
        for (int i = 0; i < fragments.length; i++) {
            if (currentTabIndex != i) {
                bt.hide(fragments[i]);
                mTabs[i].setSelected(false);
            } else {
                bt.show(fragments[i]);
                // 把当前tab设为选中状态
                mTabs[i].setSelected(true);
            }
        }
        bt.commitAllowingStateLoss();
        setToolbarTitle(titles[currentTabIndex]);
    }

    public void onTabClicked(View view) {
        int index = 0;
        switch (view.getId()) {
            case R.id.layout_home_tap_1:
                index = 0;
                break;
            case R.id.layout_home_tap_2:
                index = 1;
                break;
            case R.id.layout_home_tap_3:
                index = 2;
                break;
            case R.id.layout_home_tap_4:
                index = 3;
                break;
        }
        currentTabIndex = index;
        showSelectedTab();
    }

    //login("18202823096","123456");
    /* private void login(String userName, final String password) {

        showProgressDialog("正在登录。。。");
        //UserRequest.login(userName, password, loginCallBack);
        HttpAction.login(userName, password, new NetDataBeanCallback<LoginBean>(LoginBean.class) {
            @Override
            protected void onCodeSuccess(LoginBean data) {
                dismissProgressDialog();
                *//*AccountManager.getInstance().setUser(data.userBean);
                AccountManager.getInstance().setToken(data.token);
                AccountManager.getInstance().setUserPassWord(password);
                skip(MainActivity.class);
                finish();*//*
            }

            @Override
            protected void onCodeFailure(String msg) {
                //dismissProgressDialog();
                showToast(msg);
            }
        });
    }*/
}
