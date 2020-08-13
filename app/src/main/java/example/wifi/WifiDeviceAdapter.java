package example.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.WifiMainAdapterBinding;

import java.util.ArrayList;

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
        holder.bd.tvName.setText(data.SSID);
        holder.bd.tvAddress.setText(data.BSSID);
        holder.bd.tvLevel.setText(String.valueOf(data.level));
        holder.bd.encryptType.setText(data.capabilities);
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
