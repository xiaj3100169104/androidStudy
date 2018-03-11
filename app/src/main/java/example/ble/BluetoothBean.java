package example.ble;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;

/**
 * Created by xiajun on 2018/3/3.
 */

public class BluetoothBean {
    BluetoothDevice device;
    String deviceName;
    int profileState;
    int rssi;

    @Override
    public String toString() {
        return "BluetoothBean{" +
                "device-->address=" + device.getAddress() +
                ", type=" + device.getType() +
                ", name=" + device.getName() +
                ", bondState=" + device.getBondState() +
                ", rssi=" + rssi +
                ", profileState=" + profileState +
                '}';
    }


    public static String getTypeDesc(int type) {
        String desc = "未知";
        switch (type) {
            case BluetoothDevice.DEVICE_TYPE_UNKNOWN:
                desc = "未知蓝牙";
                break;
            case BluetoothDevice.DEVICE_TYPE_CLASSIC:
                desc = "传统蓝牙";
                break;
            case BluetoothDevice.DEVICE_TYPE_LE:
                desc = "低功耗蓝牙";
                break;
            case BluetoothDevice.DEVICE_TYPE_DUAL:
                desc = "双模蓝牙";
                break;

        }
        return desc;
    }

    public static String getBondStateDesc(int bondState) {
        String desc = "未知";
        switch (bondState) {
            case BluetoothDevice.BOND_NONE:
                desc = "未配对";
                break;
            case BluetoothDevice.BOND_BONDING:
                desc = "正在配对...";
                break;
            case BluetoothDevice.BOND_BONDED:
                desc = "已配对";
                break;
        }
        return desc;
    }

    public static String getProfileStateDesc(int state) {
        String desc = "连接";
        switch (state) {
            case BluetoothProfile.STATE_DISCONNECTED:
                desc = "未连接";
                break;
            case BluetoothProfile.STATE_CONNECTING:
                desc = "正在连接...";
                break;
            case BluetoothProfile.STATE_CONNECTED:
                desc = "已连接";
                break;
            case BluetoothProfile.STATE_DISCONNECTING:
                desc = "断开连接...";
                break;
        }
        return desc;
    }
}
