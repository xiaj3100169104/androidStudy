package example.wifi;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.AdapterBluetoothBinding;
import com.style.framework.databinding.WifiMainAdapterBinding;

import java.util.ArrayList;

import example.ble.BluetoothBean;
import example.ble.BluetoothUtil;

public class WifiDeviceAdapter extends BaseRecyclerViewAdapter<ScanResult> {
    public WifiDeviceAdapter(Context context, ArrayList<ScanResult> list) {
        super(context, list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WifiMainAdapterBinding bd = getBinding(R.layout.wifi_main_adapter, parent);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        ScanResult data = getData(position);
        logE(getTAG(), "新设备->" + data.toString());

        holder.bd.tvName.setText("");
        holder.bd.tvType.setText("" );
       /* holder.bd.tvAddress.setText("address: " + data.getAddress());
        holder.bd.tvBondState.setText(BluetoothBean.getBondStateDesc(d.getBondState()));
        holder.bd.tvRssi.setText("rssi: " + data.rssi);
        if (d.getType() == BluetoothDevice.DEVICE_TYPE_CLASSIC) {
            holder.bd.tvProfileState.setVisibility(View.GONE);
        } else if (d.getType() == BluetoothDevice.DEVICE_TYPE_LE || d.getType() == BluetoothDevice.DEVICE_TYPE_DUAL) {
            holder.bd.tvProfileState.setVisibility(View.VISIBLE);
            holder.bd.tvProfileState.setText(BluetoothBean.getProfileStateDesc(data.profileState));

        }*/
        holder.bd.tvBondState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        super.setOnItemClickListener(holder.itemView, position);
        holder.bd.executePendingBindings();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WifiMainAdapterBinding bd;

        ViewHolder(WifiMainAdapterBinding bd) {
            super(bd.getRoot());
            this.bd = bd;
        }
    }


}
