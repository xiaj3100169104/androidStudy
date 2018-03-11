package example.ble;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.AdapterBluetoothBinding;

import java.util.ArrayList;

public class BluetoothDeviceAdapter extends BaseRecyclerViewAdapter<BluetoothBean> {
    public BluetoothDeviceAdapter(Context context, ArrayList<BluetoothBean> list) {
        super(context, list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterBluetoothBinding bd = DataBindingUtil.inflate(mInflater, R.layout.adapter_bluetooth, parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        BluetoothBean data = getData(position);
        logE(TAG, "新设备->" + data.toString());
        final BluetoothDevice d = data.device;
        String name = d.getName();
        if (TextUtils.isEmpty(name)) {
            if (!TextUtils.isEmpty(data.deviceName))
                name = data.deviceName;
            else
                name = d.getAddress();
        }

        holder.bd.tvName.setText(name);
        holder.bd.tvType.setText("type: " + BluetoothBean.getTypeDesc(d.getType()));
        holder.bd.tvAddress.setText("address: " + d.getAddress());
        holder.bd.tvBondState.setText(BluetoothBean.getBondStateDesc(d.getBondState()));
        holder.bd.tvRssi.setText("rssi: " + data.rssi);
        if (d.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC) {
            holder.bd.tvProfileState.setVisibility(View.GONE);
        } else if (d.getType() == BluetoothDevice.DEVICE_TYPE_LE || d.getType() == BluetoothDevice.DEVICE_TYPE_DUAL) {
            holder.bd.tvProfileState.setVisibility(View.VISIBLE);
            holder.bd.tvProfileState.setText(BluetoothBean.getProfileStateDesc(data.profileState));

        }
        holder.bd.tvBondState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (d.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC) {
                    if (d.getBondState() == BluetoothDevice.BOND_NONE) {
                        boolean b = d.createBond();
                        logE(TAG, "createBond==" + b);
                    } else if (d.getBondState() == BluetoothDevice.BOND_BONDED) {
                        try {
                            boolean b = BluetoothUtil.removeBond(d.getClass(), d);
                            logE(TAG, "removeBond==" + b);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else if ((d.getType() == BluetoothDevice.DEVICE_TYPE_LE || d.getType() == BluetoothDevice.DEVICE_TYPE_DUAL) &&
                        d.getBondState() == BluetoothDevice.BOND_NONE) {
                    BluetoothGatt gatt = d.connectGatt(getContext(), false, gattCallback);
                }
            }
        });
        super.setOnItemClickListener(holder.itemView, position);
        holder.bd.executePendingBindings();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterBluetoothBinding bd;

        ViewHolder(AdapterBluetoothBinding bd) {
            super(bd.getRoot());
            this.bd = bd;
        }
    }


    BluetoothGattCallback gattCallback = new BluetoothGattCallback() {
        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
            logE("onConnectionStateChange", "state==" + status + "   newState==" + newState);
            if (status == 133) {
                gatt.disconnect();
                gatt.close();
                gatt.connect();
            }

            if (status == BluetoothGatt.GATT_SUCCESS && newState == BluetoothProfile.STATE_CONNECTED) {
                gatt.discoverServices();
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            logE("onServicesDiscovered", "state==" + status);
        }
    };
}
