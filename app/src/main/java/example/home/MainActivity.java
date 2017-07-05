package example.home;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.bean.User;
import com.style.framework.R;
import com.style.manager.AccountManager;
import com.style.view.CustomNotifyView;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import butterknife.Bind;
import xj.mqtt.bean.IMMessage;
import xj.mqtt.bean.MsgAction;
import xj.mqtt.db.MsgDBManager;
import xj.mqtt.service.MQTTService;
import xj.mqtt.service.MyService;

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
    @Bind(R.id.view_notify_msg)
    CustomNotifyView viewNotifyMsg;

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

    /**
     * 解决方案为以下两种：
     * 方法1：在fragmentActivity里oncreate方法判断savedInstanceState==null才生成新Fragment，否则不做处理。
     * 方法2：在fragmentActivity里重写onSaveInstanceState方法，但不做实现，也就是将super.onSaveInstanceState(outState)注释掉。
     */
   /* @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_main;
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        Intent i = new Intent(this, MQTTService.class);
        i.setAction(MQTTService.ACTION_LOGIN);
        ComponentName componentName0 = startService(i);
        componentName0.getClassName();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateUnreadMsg();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }

    @Subscriber(tag= MsgAction.MSG_NEW)
    public void onNewMsg(IMMessage msg) {
        Log.e(TAG, "onNewMsg");
        updateUnreadMsg();
    }
    @Subscriber(tag= MsgAction.MSG_UPDATE)
    public void onMsguodate(IMMessage msg) {
        Log.e(TAG, "onMsguodate");
        updateUnreadMsg();
    }
    private void updateUnreadMsg() {
        int count = MsgDBManager.getInstance().getMyUnreadAllCount(curUser.getUserId());
        viewNotifyMsg.setNotifyCount(count);
    }

    @Override
    public void initData() {
        curUser = AccountManager.getInstance().getCurrentUser();

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

        /*ComponentName componentName = startService(new Intent(this, MQTTService.class));
        componentName.getClassName();*/
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

}
