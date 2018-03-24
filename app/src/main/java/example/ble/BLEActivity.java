package example.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.ActivityBluetoothBinding;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;


public class BLEActivity extends BaseToolBarActivity {
    private static int REQUEST_ENABLE_BT = 6;

    ActivityBluetoothBinding bd;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mScanner;
    private ScanCallback mScanCallback;

    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    private ArrayList<BluetoothBean> dataList;
    private LinearLayoutManager layoutManager;
    private BluetoothDeviceAdapter adapter;
    private Handler leScanHandler = new Handler();
    private boolean isScanning;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        bd = DataBindingUtil.setContentView(this, R.layout.activity_bluetooth);
        super.setContentView(bd.getRoot());
        initData();
    }

    @Override
    public void initData() {
        setToolbarTitle("蓝牙测试");
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        if (adapter == null) {
            //系统不支持蓝牙。
            showToast("当前设备不支持蓝牙");
            finish();
            return;
        }
        boolean isSupportBle = this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        if (!isSupportBle) {
            showToast("当前设备不支持低功耗蓝牙");
            finish();
            return;
        }
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
                if (remoteDevice.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC) {
                    if (remoteDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                        //BluetoothDevice remoteDevice = adapter.getRemoteDevice(address);
                        MyGattCallback gattCallback = new MyGattCallback(data);
                        remoteDevice.connectGatt(context, true, gattCallback);
                    } else if (remoteDevice.getBondState() == BluetoothDevice.BOND_BONDED) {

                    }
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            logE(TAG, "没有权限");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                logE(TAG, "上次拒绝");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ENABLE_BT);
            } else {
                logE(TAG, "请求权限");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ENABLE_BT);
            }
        } else {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                logE(TAG, "权限允许");
                //getData2();
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

    public void openBluetooth(View v) {
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    public void close(View v) {
    }

    public void scan(View v) {
      /*  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            startLeScan();
        } else {
            startLeScan();
        }*/
        startDiscovery();
    }

    private void startDiscovery() {
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);//开启搜索
        ///filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);//配对请求
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//绑定状态改变
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//搜索结束
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        registerReceiver(mReceiver, filter);
        mBluetoothAdapter.startDiscovery();
    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            logE("onReceive", "action==" + action);
            Bundle b = intent.getExtras();
            if (b != null) {
                Object[] lstName = b.keySet().toArray();
                // 显示所有收到的消息及其细节
                for (int i = 0; i < lstName.length; i++) {
                    String keyName = lstName[i].toString();
                    Log.e(keyName, String.valueOf(b.get(keyName)));
                }
            }
            //找到设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
                Log.v("onReceive", "ACTION_FOUND:" + device.getName() + device.getAddress());
                dealData(device, device.getName(), rssi);
                // 添加进一个设备列表，进行显示。
               /* if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    Log.v(TAG, "find device:" + device.getName() + device.getAddress());
                }*/
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                logE("onReceive", "getBondState: " + device.getBondState());
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
                Log.v("onReceive", "ACTION_BOND_STATE_CHANGED:" + device.getName() + device.getAddress());
                dealData(device, device.getName(), rssi);
            } else if (intent.getAction().equals(BluetoothDevice.ACTION_PAIRING_REQUEST)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try {
                    // 广播接收者的优先者为最高 收到广播后 停止广播向下传递
                    device.setPairingConfirmation(true);
                    abortBroadcast();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }
    };

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startScan() {
        if (!isScanning) {
            mScanner = mBluetoothAdapter.getBluetoothLeScanner();
            if (mScanner == null) {
                showToast("蓝牙不可用");
                return;
            }
            mScanCallback = new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    logE("ScanCallback", "onScanResult==" + result.toString());
                    ParsedAd ad = BluetoothUtil.parseData(result.getScanRecord().getBytes());
                    dealData(result.getDevice(), ad.localName, result.getRssi());
                }

                @Override
                public void onBatchScanResults(List<ScanResult> results) {
                    logE("ScanCallback", "onBatchScanResults");
                    logResult(results);
                }

                private void logResult(List<ScanResult> results) {
                    if (results != null && results.size() > 0) {
                        for (ScanResult result : results) {
                            result.toString();
                        }
                    }
                }

                @Override
                public void onScanFailed(int errorCode) {
                    logE("ScanCallback", "onScanFailed==" + errorCode);
                }
            };
            mScanner.startScan(mScanCallback);
            isScanning = true;
            leScanHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScan(null); //停止扫描
                }
            }, 5000);//设置10秒钟结束扫描
        }
    }

    private void startLeScan() {
        //正在扫描时，不执行扫描
        if (!isScanning) {
            mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
                @Override
                public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                    //这里注意，本人在开发中遇到的是 经常有的蓝牙设备是没有名字的， （device.getName == null）
                    //不知道这是什么原因引起的，后来跟很多蓝牙高手讨论的是结果初步怀疑应该是芯片的问题
                    //尤其是MTK的芯片经常出现这种问题,换了搭载高通和华为的芯片的设备就没问题了。
                    //logE("LeScanCallback", "onLeScan=" + scanRecord.length);
                    ParsedAd ad = BluetoothUtil.parseData(scanRecord);
                    dealData(device, ad.localName, rssi);
                }
            };
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            isScanning = true;
            leScanHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    stopScan(null); //停止扫描
                }
            }, 5000);//设置10秒钟结束扫描
        }

    }

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

    public void stopScan(View v) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        } else {
            if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
                mScanner.stopScan(mScanCallback);
        }
        isScanning = false;
    }

}
