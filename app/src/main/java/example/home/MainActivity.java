package example.home;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.style.base.BaseToolBarActivity;
import com.style.bean.User;
import com.style.framework.R;
import com.style.framework.databinding.ActivityMainBinding;
import com.style.manager.AccountManager;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import example.ble.BleManager;


public class MainActivity extends BaseToolBarActivity {
    ActivityMainBinding bd;
    public static final int REQUEST_ENABLE_BT = 6;

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

    /**
     * 解决方案为以下两种：
     * 方法1：在fragmentActivity里oncreate方法判断savedInstanceState==null才生成新Fragment，否则不做处理。
     * 方法2：在fragmentActivity里重写onSaveInstanceState方法，但不做实现，也就是将super.onSaveInstanceState(outState)注释掉。
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {

        //super.onSaveInstanceState(outState);//将super调用取消即可，表明当意外(比如系统内存吃紧将应用杀死)发生我不需要保存Fragment状态和数据等,当activity销毁时不保存其内部的view的状态
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_main);

        EventBus.getDefault().register(this);
        super.setContentView(bd.getRoot());
        initData();
        /*Intent i = new Intent(this, MQTTService.class);
        i.setAction(MQTTService.ACTION_LOGIN);
        ComponentName componentName0 = startService(i);
        componentName0.getClassName();*/
    }

    @Override
    protected void onResume() {
        super.onResume();
        //updateUnreadMsg();
    }

    @Override
    protected void onDestroy() {
        //取消事件注册
        EventBus.getDefault().unregister(this);
        BleManager.getInstance().close();
        super.onDestroy();

    }

    @Override
    public void initData() {
        String[] permissions = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};

        if (ContextCompat.checkSelfPermission(this.getApplication(), permissions[0]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this.getApplication(), permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            logE(TAG, "没有权限");
            /*if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }*/
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1])) {
                logE(TAG, "上次拒绝");
                ActivityCompat.requestPermissions(this, permissions, REQUEST_ENABLE_BT);
            } else {
                logE(TAG, "请求权限");
                ActivityCompat.requestPermissions(this, permissions, REQUEST_ENABLE_BT);
            }
        } else {
            startBleService();
        }
        curUser = AccountManager.getInstance().getCurrentUser();

        mTabs = new TextView[5];
        mTabs[0] = bd.viewHomeTap1;
        mTabs[1] = bd.viewHomeTap2;
        mTabs[2] = bd.viewHomeTap3;
        mTabs[3] = bd.viewHomeTap4;
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

    private void startBleService() {
        BleManager.getInstance().init(this);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                logE(TAG, "权限允许");
                startBleService();
            } else {
                logE(TAG, "权限拒绝");
                // Permission Denied
                showToast("权限拒绝");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
