package example.ble;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.style.app.FileDirConfig;
import com.style.base.BaseDefaultTitleBarActivity;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.ActivityBluetoothBinding;
import com.style.view.systemHelper.DividerItemDecoration;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


public class BlueToothActivity extends BaseDefaultTitleBarActivity {
    private static int REQUEST_ENABLE_BT = 6;

    ActivityBluetoothBinding bd;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothLeScanner mScanner;
    private ScanCallback mScanCallback;

    private BluetoothAdapter.LeScanCallback mLeScanCallback;

    private ArrayList<BluetoothBean> dataList;
    private LinearLayoutManager layoutManager;
    private BluetoothDeviceAdapter adapter;
    private File f;
    private BufferedWriter bufferedReader;

    @Override
    public int getLayoutResId() {
        return R.layout.activity_bluetooth;
    }


    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("蓝牙测试");
        // Initializes Bluetooth adapter.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        dataList = new ArrayList<>();
        adapter = new BluetoothDeviceAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        bd.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener<BluetoothBean>() {
            @Override
            public void onItemClick(int position, BluetoothBean data) {
                final BluetoothDevice d = data.device;
                if (d.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC) {
                    if (d.getBondState() == BluetoothDevice.BOND_NONE) {

                    } else if (d.getBondState() == BluetoothDevice.BOND_BONDED) {
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    BluetoothSocket socket = d.createInsecureRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                                    socket.connect();
                                    socket.getOutputStream().write(5);

                                    /*BluetoothServerSocket server = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord("myServerSocket",
                                            UUID.fromString("84D1319C-FBAF-644C-901A-8F091F25AF04"));
                                    BluetoothSocket socket = server.accept();
                                    InputStream inputStream = socket.getInputStream();
                                    int read = -1;
                                    final byte[] bytes = new byte[1024];
                                    for (; (read = inputStream.read(bytes)) > -1; ) {
                                        final int count = read;

                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 0; i < count; i++) {
                                            if (i > 0) {
                                                sb.append(' ');
                                            }
                                            String _s = Integer.toHexString(bytes[i] & 0xFF);
                                            if (_s.length() < 2) {
                                                sb.append('0');
                                            }
                                            sb.append(_s);
                                        }
                                        System.out.println(sb.toString());

                                    }
                                    socket.getOutputStream().write(5);
                                    socket.close();*/
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }
                }
            }
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            logE(getTAG(), "没有权限");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                logE(getTAG(), "上次拒绝");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ENABLE_BT);
            } else {
                logE(getTAG(), "请求权限");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_ENABLE_BT);
            }
        } else {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                logE(getTAG(), "权限允许");
                //getData2();
            } else {
                logE(getTAG(), "权限拒绝");
                // Permission Denied
                showToast("Permission Denied");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void openBluetooth(View v) {
        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (mBluetoothAdapter == null || !mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

    }

    public void scan(View v) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            startLeScan();
        } else {
            startLeScan();
            //startScan();
        }
        //startDiscovery();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void startScan() {
        mScanner = mBluetoothAdapter.getBluetoothLeScanner();
        if (mScanner == null) {
            showToast("蓝牙不可用");
            return;
        }
        mScanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                logE("ScanCallback", "onScanResult==" + result.toString());
                //ParsedAd ad = BluetoothUtil.parseData(result.getScanRecord().getBytes());
                //dealData(result.getDevice(), ad.localName, result.getRssi());
            }

            @Override
            public void onBatchScanResults(List<ScanResult> results) {
                logE("ScanCallback", "onBatchScanResults");
                //logResult(results);
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
    }

    private void startLeScan() {
        String name = "blueInfo" + new SimpleDateFormat("yyyy-MM-dd_hh_mm_ss").format(new Date()) + ".csv";
        f = new File(FileDirConfig.DIR_APP + "/" + name);
        if (!f.getParentFile().exists())
            f.getParentFile().mkdirs();
        FileWriter fileReader = null;
        try {
            fileReader = new FileWriter(f, true);
            bufferedReader = new BufferedWriter(fileReader);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
            @Override
            public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
                logE("LeScanCallback", device.toString() + "  " + rssi);
                String macAddress = device.toString().replace(":", "");
                if (macAddress.startsWith("1918")) {
                    String s = new StringBuffer().append(String.valueOf(System.currentTimeMillis())).append(",").append(macAddress).append(",").append(String.valueOf(rssi)).toString();
                    saveAndNewLine(s);
                }
                //ParsedAd ad = BluetoothUtil.parseData(scanRecord);
                //dealData(device, ad.localName, rssi);
            }
        };
        mBluetoothAdapter.startLeScan(mLeScanCallback);
    }

    public void saveAndNewLine(String s) {

        try {
            bufferedReader.newLine();
            bufferedReader.write(s);
            bufferedReader.flush();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }

    }

    public void stopScan(View v) {
        mBluetoothAdapter.stopLeScan(mLeScanCallback);

        /*if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        } else {
            if (mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
                mScanner.stopScan(mScanCallback);
        }*/
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

    //scanRecords的格式转换
    static final char[] hexArray = "0123456789ABCDEF".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public void close(View v) {
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
}
