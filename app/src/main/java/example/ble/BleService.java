package example.ble;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.style.data.prefs.AccountManager;
import com.style.app.LogManager;

import org.simple.eventbus.EventBus;

import java.util.ArrayList;

/**
 * Created by xiajun on 2018/4/17.
 */

public class BleService extends Service {
    private final String TAG = getClass().getSimpleName();

    public static final String SCAN_START = "scan_start";
    public static final String SCAN_FOUND_DEVICE = "scan_found_device";
    public static final String SCAN_END = "scan_end";

    public static final int STATE_DISCONNECTED = 0;
    public static final int STATE_CONNECTING = 1;
    public static final int STATE_CONNECTED = 2;
    /**
     * 自定义Binder对象，用以绑定服务的时候，获取服务的实例
     */
    private BleServiceBinder mServiceBinder;
    private BluetoothAdapter mBluetoothAdapter;
    private MyGattCallback gattCallback;
    private BluetoothGatt gatt;
    private int mState = STATE_DISCONNECTED;
    private ArrayList<BluetoothBean> deviceList = new ArrayList<>();
    //是否自动连接,默认true
    private boolean isAutoConnect = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mServiceBinder == null)
            mServiceBinder = new BleServiceBinder();
        return mServiceBinder;
    }

    public class BleServiceBinder extends Binder {
        /**
         * 获取蓝牙连接服务实例
         *
         * @return
         */
        public BleService getBleService() {
            return BleService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);//开启搜索
        ///filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);//配对请求
        //filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//绑定状态改变
        filter.addAction(BluetoothDevice.ACTION_UUID);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙开关状态发生了变化
        //filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//扫描模式:比如是否能被发现
        //蓝牙广播是系统广播不能用本地广播
        registerReceiver(mReceiver, filter);
        initEnable();
        connectByScan();
    }

    private void initEnable() {
        logE(TAG, "初始化蓝牙");
        BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        if (bluetoothManager != null && mBluetoothAdapter == null)
            mBluetoothAdapter = bluetoothManager.getAdapter();

        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
        }
    }
    //
    public void connectByScan() {
        logE(TAG, "先扫描，再连接");
        String address = AccountManager.Companion.getInstance().getBleAddress();
        /*if (TextUtils.isEmpty(address)) {
            logE(TAG, "address-->   " + address);
            return;
        }*/
        scanDevice();
    }

    public void scanDevice() {
        logE(TAG, "开始扫描");
        mBluetoothAdapter.startDiscovery();

    }

    //只有扫描到周围有与保存的蓝牙地址相同的蓝牙设备，才能进行连接
    public void connect() {
        logE(TAG, "开始连接");
        String address = AccountManager.Companion.getInstance().getBleAddress();
        //处于连接断开时才进行连接
        if (mState == STATE_DISCONNECTED) {
            isAutoConnect = true;
            try {
                setStateConnecting();
                BluetoothDevice remoteDevice = mBluetoothAdapter.getRemoteDevice(address);
                gattCallback = new MyGattCallback(this);
                gatt = remoteDevice.connectGatt(this, false, gattCallback);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                setStateDisconnected();
            }
        }
    }

    public void reConnect() {
        if (isAutoConnect) {
            logE(TAG, "开始重新连接");
            connect();
        }
    }

    //扫描完成自动连接
    private void connectAuto() {
        if (isAutoConnect) {
            logE(TAG, "开始自动连接");
            connect();
        }
    }

    //蓝牙打开后可直接进行扫描操作，
    private void bluetoothOpened() {
        logE(TAG, "蓝牙已经打开");
        scanDevice();
    }

    //蓝牙已经关闭,不需要进行自动连接
    private void bluetoothClosed() {
        logE(TAG, "蓝牙已经关闭");
        setAutoConnect(false);
    }

    public void disconnect() {
        if (gatt != null) {
            gatt.disconnect();
            gatt.close();
            gatt = null;
        }
    }

    public void setStateDisconnected() {
        mState = STATE_DISCONNECTED;
    }

    public void setStateConnecting() {
        mState = STATE_CONNECTING;
    }

    public void setStateConnected() {
        mState = STATE_CONNECTED;
    }

    public void setAutoConnect(boolean b) {
        isAutoConnect = false;
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
            } /*else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                short rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, (short) 0);
                int bondState = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.BOND_NONE);

                Log.e("BOND_STATE_CHANGED", "ACTION_BOND_STATE_CHANGED:" + device.getName() + device.getAddress() + "  bondState: " + bondState);
                dealData(device, device.getName(), rssi);
            } */ else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mBluetoothAdapter.cancelDiscovery();
                EventBus.getDefault().post("", SCAN_END);
                connectAuto();
            }else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                deviceList.clear();
                EventBus.getDefault().post("", SCAN_START);
            }
        }
    };

    private void dealData(BluetoothDevice device, String deviceName, int rssi) {
        BluetoothBean b = new BluetoothBean();
        b.device = device;
        b.deviceName = deviceName;
        b.rssi = rssi;
        boolean isExist = false;
        for (int i = 0; i < deviceList.size(); i++) {
            if (device.getAddress().equals(deviceList.get(i).device.getAddress())) {
                isExist = true;
                deviceList.set(i, b);
                break;
            }
        }
        if (!isExist) {
            deviceList.add(b);
            EventBus.getDefault().post(b, SCAN_FOUND_DEVICE);
        }
    }

    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        disconnect();
        super.onDestroy();
    }

    public void logE(String tag, String msg) {
        LogManager.logE(tag, msg);
    }
}
