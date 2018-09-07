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
        AdapterBluetoothBinding bd = getBinding(R.layout.adapter_bluetooth, parent);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        BluetoothBean data = getData(position);
        //logE(TAG, "新设备->" + data.toString());
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
}
