package example.ble;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.base.BaseToolBarActivity;
import com.style.framework.R;
import com.style.framework.databinding.BleActivityScanBinding;
import com.style.manager.AccountManager;
import com.style.view.DividerItemDecoration;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;


public class BLEActivity extends BaseToolBarActivity {
    public static final int REQUEST_ENABLE_BT = 6;

    BleActivityScanBinding bd;
    private ArrayList<BluetoothBean> dataList;
    private LinearLayoutManager layoutManager;
    private BluetoothDeviceAdapter adapter;
    private Handler leScanHandler = new Handler();
    private boolean isScanning;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        bd = DataBindingUtil.setContentView(this, R.layout.ble_activity_scan);
        super.setContentView(bd.getRoot());
        initData();
        EventBus.getDefault().register(this);
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
                Log.e(TAG, "address-->" + remoteDevice.getAddress() + "   " + "type-->" + remoteDevice.getType());
                if (remoteDevice.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                    if (remoteDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                        AccountManager.getInstance().saveBleAddress(remoteDevice.getAddress());
                        BleManager.getInstance().connect();
                    } else if (remoteDevice.getBondState() == BluetoothDevice.BOND_BONDED) {

                    }
                }
            }
        });

    }

    @Subscriber(tag = BleService.SCAN_START)
    public void scanStart(String data) {
        isScanning = true;
        dataList.clear();
        adapter.notifyDataSetChanged();
    }

    @Subscriber(tag = BleService.SCAN_FOUND_DEVICE)
    public void foundDevice(BluetoothBean data) {
        dealData(data);
    }

    @Subscriber(tag = BleService.SCAN_END)
    public void scanEnd(String data) {
        isScanning = false;
    }

    private void dealData(BluetoothBean data) {
        boolean isExist = false;
        for (int i = 0; i < dataList.size(); i++) {
            if (data.device.getAddress().equals(dataList.get(i).device.getAddress())) {
                isExist = true;
                dataList.set(i, data);
                adapter.notifyItemChanged(i);
                break;
            }
        }
        if (!isExist) {
            dataList.add(data);
            adapter.notifyDataSetChanged();
        }
    }


    public void disconnect(View v) {
        BleManager.getInstance().disconnect();
    }

    public void scan(View v) {
        String[] permissions = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};

        if (ContextCompat.checkSelfPermission(this.getApplication(), permissions[0]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this.getApplication(), permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            logE(TAG, "没有权限");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1])) {
                logE(TAG, "上次拒绝");
                ActivityCompat.requestPermissions(this, permissions, REQUEST_ENABLE_BT);
            } else {
                logE(TAG, "请求权限");
                ActivityCompat.requestPermissions(this, permissions, REQUEST_ENABLE_BT);
            }
        } else {
            BleManager.getInstance().scan();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_ENABLE_BT) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                logE(TAG, "权限允许");
                BleManager.getInstance().scan();
            } else {
                logE(TAG, "权限拒绝");
                // Permission Denied
                showToast("权限拒绝");
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        leScanHandler.removeMessages(0);

    }
}
