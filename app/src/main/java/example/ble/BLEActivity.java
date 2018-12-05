package example.ble;

import android.Manifest;
import android.bluetooth.BluetoothDevice;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.style.base.BaseTitleBarActivity;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.data.prefs.AppPrefsManager;
import com.style.framework.R;
import com.style.framework.databinding.BleActivityScanBinding;
import com.style.view.systemHelper.DividerItemDecoration;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;


public class BLEActivity extends BaseTitleBarActivity {
    public static final int REQUEST_ENABLE_BT = 6;

    BleActivityScanBinding bd;
    private ArrayList<BluetoothBean> dataList;
    private LinearLayoutManager layoutManager;
    private BluetoothDeviceAdapter adapter;
    private Handler leScanHandler = new Handler();
    private boolean isScanning;

    @Override
    public int getLayoutResId() {
        return R.layout.ble_activity_scan;
    }

    @Override
    public void initData() {
        bd = getBinding();
        setToolbarTitle("低功耗蓝牙连接测试");
        EventBus.getDefault().register(this);

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
                Log.e(getTAG(), "address-->" + remoteDevice.getAddress() + "   " + "type-->" + remoteDevice.getType());
                if (remoteDevice.getType() == BluetoothDevice.DEVICE_TYPE_LE) {
                    if (remoteDevice.getBondState() == BluetoothDevice.BOND_NONE) {
                        AppPrefsManager.Companion.getInstance().saveBleAddress(remoteDevice.getAddress());
                        BleManager.getInstance().connect();
                    } else if (remoteDevice.getBondState() == BluetoothDevice.BOND_BONDED) {

                    }
                }
            }
        });
        bd.btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scan();
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

    public void scan() {
        String[] permissions = {Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN};

        if (ContextCompat.checkSelfPermission(this.getApplication(), permissions[0]) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this.getApplication(), permissions[1]) != PackageManager.PERMISSION_GRANTED) {
            logE(getTAG(), "没有权限");
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])
                    || ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[1])) {
                logE(getTAG(), "上次拒绝");
                ActivityCompat.requestPermissions(this, permissions, REQUEST_ENABLE_BT);
            } else {
                logE(getTAG(), "请求权限");
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
                logE(getTAG(), "权限允许");
                BleManager.getInstance().scan();
            } else {
                logE(getTAG(), "权限拒绝");
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
