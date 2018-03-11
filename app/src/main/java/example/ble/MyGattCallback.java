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
    //uuid需要替换成项目中使用的uuid，这只是举个例子
    private static final String serviceid = "0000fee7-0000-1000-8000-00805f9b34fb";
    private static final String charaid   = "0000feaa-0000-1000-8000-00805f9b34fb";
    private static final String notifyid  = "00001202-0000-1000-8000-00805f9b34fb";

    private static UUID SERVICE_UUID = UUID.fromString(serviceid);
    private static UUID CHARACTERISTIC_UUID = UUID.fromString(charaid);
    private static UUID NOTIFY_UUID = UUID.fromString(notifyid);

    private BluetoothBean bluetoothBean;


    public MyGattCallback(BluetoothBean bluetoothBean) {
        this.bluetoothBean = bluetoothBean;
    }

    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (newState == BluetoothProfile.STATE_CONNECTED) {
            //连接成功
            gatt.discoverServices();//开始发现设备的服务
        } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
            //连接断开
        }
        //改变当前状态
        this.bluetoothBean.profileState = newState;
        //adapter.notifyDataSetChanged();
    }

    @Override
    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        ////找到了服务，此时才是真正的连接上了设备
        if (status == BluetoothGatt.GATT_SUCCESS) {
            BluetoothGattService service = gatt.getService(SERVICE_UUID); //通过厂家给的UUID获取BluetoothGattService
            if (service == null) {
                Log.e("onServicesDiscovered", "未找到蓝牙中的对应服务");
                return;
            }
            BluetoothGattCharacteristic characteristic = service.getCharacteristic(CHARACTERISTIC_UUID);//同上
            if (characteristic == null) {
                Log.e("onServicesDiscovered", "未找到蓝牙中的对应特征");
                return;
            }
          /*  //读取数据
            byte[] data = characteristic.getValue();
            characteristic.setValue(CHexConver.hexStr2Bytes("TEST"));
            //每次最多传20bytes，每次最少间隔10ms。
            gatt.writeCharacteristic(characteristic);*/
            if ((characteristic.getProperties() | BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                //设置true为启用通知,//设置characteristic的通知，触发bluetoothGatt.onCharacteristicWrite()事件。
                boolean success = gatt.setCharacteristicNotification(characteristic, true);
                Log.e("success", "setCharactNotify: "+success);
                //在通过上面的设置返回为true之后还要进行下面的操作，才能订阅到数据的上传。下面是完整的订阅数据代码！
                if (success) {
                    for (BluetoothGattDescriptor dp : characteristic.getDescriptors()) {
                        if (dp != null) {
                            if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0) {
                                dp.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
                            } else if ((characteristic.getProperties() & BluetoothGattCharacteristic.PROPERTY_INDICATE) != 0) {
                                dp.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
                            }
                            gatt.writeDescriptor(dp);
                        }
                    }
                }
            }

        }
    }

    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);
        String value = CHexConver.byte2HexStr(characteristic.getValue(), characteristic.getValue().length);
        Log.e(TAG, gatt.getDevice().getName() + " recieved " + value);
    }

    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);
        String response = CHexConver.byte2HexStr(characteristic.getValue(), characteristic.getValue().length);
        Log.e(TAG, "The response is " + response);
    }

    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
        Log.e(TAG, gatt.getDevice().getName() + " write successfully");
    }

}
