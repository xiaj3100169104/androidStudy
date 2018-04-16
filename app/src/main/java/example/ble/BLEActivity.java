package example.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.BleActivityScanBinding;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;


public class BLEActivity extends BaseToolBarActivity {
    private static int REQUEST_ENABLE_BT = 6;

    BleActivityScanBinding bd;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mScanner;
    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    private ArrayList<BluetoothBean> dataList;
    private LinearLayoutManager layoutManager;
    private BluetoothDeviceAdapter adapter;
    private Handler leScanHandler = new Handler();
    private boolean isScanning;
    private MyGattCallback gattCallback;
    private BluetoothGatt gatt;
    private String address;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        bd = DataBindingUtil.setContentView(this, R.layout.ble_activity_scan);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("低功耗蓝牙连接测试");
        dataList = new ArrayList<>();
        adapter = new BluetoothDeviceAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        bd.recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<BluetoothBean>() {
            @Override
            public void onItemClick(int position, BluetoothBean data) {
                BluetoothDevice remoteDevice = data.device;
                if (remoteDevice.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                    if (remoteDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                        BLEActivity.this.address = remoteDevice.getAddress();
                        prepareConnect();
                    } else if (remoteDevice.getBondState() == BluetoothDevice.BOND_BONDED) {

                    }
                }
            }
        });

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);//开启搜索
        ///filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);//配对请求
        //filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//绑定状态改变
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索结束
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙开关状态发生了变化
        //filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//扫描模式:比如是否能被发现
        registerReceiver(mReceiver, filter);
        prepareConnect();
    }

    //准备连接
    private void prepareConnect() {
        logE(TAG, "准备连接");
        if (!hasPermissions()) {
            showToast("没有蓝牙权限");
            return;
        }
        if (!isBleEnable()) {
            openBle();
            return;
        }
        startDiscovery();
    }

    private void openBle() {
        logE(TAG, "打开蓝牙");
        mBluetoothAdapter.enable();
    }

    //只有扫描到周围有与保存的蓝牙地址相同的蓝牙设备，才能进行连接
    private void connect() {
        logE(TAG, "开始连接");
        try {
            BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice(address);
            gattCallback = new MyGattCallback(address);
            gatt = remoteDevice.connectGatt(context, false, gattCallback);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    //蓝牙打开后可直接进行扫描操作，
    private void bluetoothOpened() {
        logE(TAG, "蓝牙已经打开");
        startDiscovery();
    }

    //蓝牙已经关闭
    private void bluetoothClosed() {
        logE(TAG, "蓝牙已经关闭");

    }

    private boolean isBleEnable() {
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (mBluetoothAdapter.isEnabled())
            return true;
        return false;
    }

    public void close(View v) {
        gatt.disconnect();
        //gatt.close();
    }

    public void scan(View v) {
        prepareConnect();
    }

    private void startDiscovery() {
        logE(TAG, "开始扫描");
        mBluetoothAdapter.startDiscovery();
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
            logE("onReceive", "action==" + action + "   state-->" + state);
            //先判断action，再判断状态值
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
                if (state == BluetoothAdapter.STATE_ON) {
                    bluetoothOpened();
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    bluetoothClosed();
                }
            //找到设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
                Log.e("ACTION_FOUND", device.getName() + "  type:" + "  " + device.getAddress() + "-->信号强度：" + rssi);
                dealData(device, device.getName(), rssi);
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
                int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);

                Log.e("BOND_STATE_CHANGED", "ACTION_BOND_STATE_CHANGED:" + device.getName() + device.getAddress() + "  bondState: " + bondState);
                dealData(device, device.getName(), rssi);
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mBluetoothAdapter.cancelDiscovery();
                connect();
            }/*else if (intent.getAction().equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try {
                    // 广播接收者的优先者为最高 收到广播后 停止广播向下传递
                    device.setPairingConfirmation(true);
                    abortBroadcast();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
        }
    };

    private void dealData(BluetoothDevice device, String deviceName, int rssi) {
        BluetoothBean b = new BluetoothBean();
        b.device = device;
        b.deviceName = deviceName;
        b.rssi = rssi;
        boolean isExist = false;
        for (int i = 0; i < dataList.size(); i++) {
            if (device.getAddress().equals(dataList.get(i).device.getAddress())) {
                isExist = true;
                dataList.set(i, b);
                adapter.notifyItemChanged(i);
                break;
            }
        }
        if (!isExist) {
            dataList.add(b);
            adapter.notifyDataSetChanged();
        }
    }

    private boolean hasPermissions() {
        logE(TAG, "检查权限");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH) != PackageManager.PERMISSION_GRANTED) {
            logE(TAG, "没有权限");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.BLUETOOTH)) {
                logE(TAG, "上次拒绝");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, REQUEST_ENABLE_BT);
            } else {
                logE(TAG, "请求权限");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.BLUETOOTH}, REQUEST_ENABLE_BT);
            }
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                logE(TAG, "权限允许");
                openBle();
            } else {
                logE(TAG, "权限拒绝");
                // Permission Denied
                showToast("Permission Denied");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
