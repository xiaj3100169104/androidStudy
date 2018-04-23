package example.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.util.Log;

import java.util.UUID;

/**
 * Created by xiajun on 2018/3/11.
 */

public class MyGattCallback extends BluetoothGattCallback {
    private static final String TAG = "MyGattCallback";

    private static final UUID UUID_SERVICE = UUID.fromString("66880000-0000-1000-8000-008012563489");
    private static final UUID UUID_CHARACTERISTIC_WRITE = UUID.fromString("66880001-0000-1000-8000-008012563489");
    private static final UUID UUID_CHARACTERISTIC_NOTIFY = UUID.fromString("66880002-0000-1000-8000-008012563489");
    private static final UUID UUID_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
    private final BleService mService;

    private BluetoothGattCharacteristic characteristicWrite;


    public MyGattCallback(BleService service) {
        this.mService = service;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        Log.e(TAG, "onConnectionStateChange ------------" + status + "---" + newState);
        if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
            //连接成功
            gatt.discoverServices();//开始发现设备的服务
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            Log.e(TAG, "onConnectionStateChange ------------" + " 连接断开");
            //连接断开
            mService.setStateDisconnected();
            mService.reConnect();
        }
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        Log.e(TAG, "onServicesDiscovered ----------------" + status);
        ////找到了服务，此时才是真正的连接上了设备
        if (status == BluetoothGatt.GATT_SUCCESS) {
            BluetoothGattService service = gatt.getService(UUID_SERVICE); //通过厂家给的UUID获取BluetoothGattService
            if (service == null) {
                Log.e("onServicesDiscovered", "未找到相应的蓝牙服务");
                return;
            }
            characteristicWrite = service.getCharacteristic(UUID_CHARACTERISTIC_WRITE);//同上
            if (characteristicWrite == null) {
                Log.e("onServicesDiscovered", "未找到蓝牙的写特征");
                return;
            }
            BluetoothGattCharacteristic characteristicNotify = service.getCharacteristic(UUID_CHARACTERISTIC_NOTIFY);
            if (characteristicNotify == null) {
                Log.e("onServicesDiscovered", "未找到蓝牙的通知特征");
                return;
            }
            gatt.setCharacteristicNotification(characteristicNotify, true);
            BluetoothGattDescriptor descriptor = characteristicNotify.getDescriptor(UUID_CONFIG);
            if (descriptor == null) {
                Log.e("onServicesDiscovered", "未找到蓝牙的配置特征");
                return;
            }
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            gatt.writeDescriptor(descriptor);
        }
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        Log.e(TAG, "onDescriptorWrite ----------------" + status);
        Log.e(TAG, gatt.getDevice().getName() + " 连接成功");
        //这里真正连接成功
        mService.setStateConnected();

    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        Log.e(TAG, "onCharacteristicRead ----------------" + status);
        String value = CHexConver.byte2HexStr(characteristic.getValue(), characteristic.getValue().length);
        Log.e(TAG, gatt.getDevice().getName() + " recieved " + value);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        Log.e(TAG, "onCharacteristicRead connect----------------" + gatt.connect());
        String response = CHexConver.byte2HexStr(characteristic.getValue(), characteristic.getValue().length);
        Log.e(TAG, "The response is " + response);
    }


}
