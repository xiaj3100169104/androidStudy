package example.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.style.base.BaseActivityPresenter;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseActivity;
import com.style.framework.R;
import com.style.framework.databinding.WifiActivityMainBinding;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import example.ble.BluetoothBean;
import example.ble.BluetoothDeviceAdapter;


public class WifiTestActivity extends BaseActivity {
    private static int REQUEST_ENABLE_BT = 6;
    WifiActivityMainBinding bd;

    private ArrayList<ScanResult> dataList;
    private LinearLayoutManager layoutManager;
    private WifiDeviceAdapter adapter;
    private Handler leScanHandler = new Handler();
    private boolean isScanning;
    private WifiManager mWifiManager;
    private WifiStateReceiver mReceiver;
    private List<ScanResult> mScanResults;//扫描结果

    @Override
    protected BaseActivityPresenter getPresenter() {
        return null;
    }

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        bd = DataBindingUtil.setContentView(this, R.layout.wifi_activity_main);
        super.setContentView(bd.getRoot());
    }

    @Override
    public void initData() {
        setToolbarTitle("蓝牙测试");
        dataList = new ArrayList<>();
        adapter = new WifiDeviceAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        bd.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<ScanResult>() {
            @Override
            public void onItemClick(int position, ScanResult data) {

            }
        });
        openWifi();
        registerWifiRecv();

    }


    public void openBluetooth(View v) {
        openWifi();
    }

    private void openWifi() {
        //如果使用activity的context则不能访问存储空间，在版本大于Android N时,因此使用全局的Context
        mWifiManager = (WifiManager) this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (!mWifiManager.isWifiEnabled()) {
            mWifiManager.setWifiEnabled(true);
        }

    }

    private void registerWifiRecv() {
        //注册广播
        mReceiver = new WifiStateReceiver();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(WifiManager.RSSI_CHANGED_ACTION); //信号强度变化
        mFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION); //网络状态变化
        mFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION); //wifi状态，是否连上，密码
        mFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION); //是不是正在获得IP地址
        mFilter.addAction(WifiManager.NETWORK_IDS_CHANGED_ACTION);
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        this.registerReceiver(mReceiver, mFilter);
    }

    public void close(View v) {
    }

    public void scan(View v) {
        mWifiManager.startScan();
        mScanResults = mWifiManager.getScanResults();
        dataList.addAll(mScanResults);
        adapter.notifyDataSetChanged();
    }

    private void dealData() {

    }

    class WifiStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*String action = intent.getAction();
            switch (action) {
                case WifiManager.RSSI_CHANGED_ACTION:
                    //信号强度变化
                    mWifiStateChangeListener.onSignalStrengthChanged(getStrength(context));
                    break;
                case WifiManager.NETWORK_STATE_CHANGED_ACTION:
                    NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                    if (info.getDetailedState().equals(NetworkInfo.DetailedState.DISCONNECTED)) {
                        //wifi已断开
                        mWifiStateChangeListener.onWifiDisconnect();
                    } else if (info.getDetailedState().equals(NetworkInfo.DetailedState.CONNECTING)) {
                        //正在连接...
                        mWifiStateChangeListener.onWifiConnecting();
                    } else if (info.getDetailedState().equals(NetworkInfo.DetailedState.CONNECTED)) {
                        //连接到网络
                        mWifiStateChangeListener.onWifiConnected();
                    } else if (info.getDetailedState().equals(NetworkInfo.DetailedState.OBTAINING_IPADDR)) {
                        //正在获取IP地址
                        mWifiStateChangeListener.onWifiGettingIP();
                    } else if (info.getDetailedState().equals(NetworkInfo.DetailedState.FAILED)) {
                        //连接失败
                    }

                    break;
                case WifiManager.WIFI_STATE_CHANGED_ACTION:
                    int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                    switch (wifiState) {
                        case WifiManager.WIFI_STATE_ENABLING:
                            //wifi正在启用
                            mWifiStateChangeListener.onWifiEnabling();
                            break;
                        case WifiManager.WIFI_STATE_ENABLED:
                            //Wifi已启用
                            mWifiStateChangeListener.onWifiEnable();
                            break;
                    }
                    break;
                case WifiManager.SUPPLICANT_STATE_CHANGED_ACTION:
                    int error = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, -100);
                    Log.e("WifiStateReceiver", "密码认证错误：" + error + "\n");
                    if (error == WifiManager.ERROR_AUTHENTICATING) {
                        //wifi密码认证错误！
                        mWifiStateChangeListener.onPasswordError();
                    }
                    break;
                case WifiManager.NETWORK_IDS_CHANGED_ACTION:
                    //已经配置的网络的ID可能发生变化时
                    mWifiStateChangeListener.onWifiIDChange();
                    break;
                case ConnectivityManager.CONNECTIVITY_ACTION:
                    //连接状态发生变化，暂时没用到
                    int type = intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, 0);
                    break;
                default:
                    break;
            }*/
        }
    }

    /**
     * 计算信号强度
     *
     * @param context 含有WIFI信息的资源对象
     * @return 信号强度
     */
    private int getStrength(Context context) {
        WifiInfo info = getConnectInfo();
        if (info.getBSSID() != null) {
            int strength = WifiManager.calculateSignalLevel(info.getRssi(), 5);
            // 链接速度
//          int speed = info.getLinkSpeed();
//          // 链接速度单位
//          String units = WifiInfo.LINK_SPEED_UNITS;
//          // Wifi源名称
//          String ssid = info.getSSID();
            return strength;
        }
        return 0;
    }

    public WifiInfo getConnectInfo() {
        return mWifiManager.getConnectionInfo();
    }


    public void stopScan(View v) {

        isScanning = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        leScanHandler.removeMessages(0);

    }
}
