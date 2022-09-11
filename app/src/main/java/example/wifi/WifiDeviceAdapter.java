package example.wifi;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.databinding.WifiMainAdapterBinding;

import java.util.ArrayList;

public class WifiDeviceAdapter extends BaseRecyclerViewAdapter<ScanResult> {

    public WifiDeviceAdapter(Context context, ArrayList<ScanResult> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        WifiMainAdapterBinding bd = WifiMainAdapterBinding.inflate(getLayoutInflater(), parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        ScanResult data = getList().get(position);
        holder.bd.tvName.setText(data.SSID);
        holder.bd.tvAddress.setText(data.BSSID);
        holder.bd.tvLevel.setText(String.valueOf(data.level));
        holder.bd.encryptType.setText(data.capabilities);
        super.setOnItemClickListener(holder.itemView, position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        WifiMainAdapterBinding bd;

        ViewHolder(WifiMainAdapterBinding bd) {
            super(bd.getRoot());
            this.bd = bd;
        }
    }

}
