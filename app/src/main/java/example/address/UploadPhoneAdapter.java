package example.address;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.databinding.AdapterAddressBinding;

import java.util.ArrayList;

public class UploadPhoneAdapter extends BaseRecyclerViewAdapter<UploadPhone> {

	public UploadPhoneAdapter(Context context, ArrayList<UploadPhone> list) {
		super(context, list);
	}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterAddressBinding bd = AdapterAddressBinding.inflate(getLayoutInflater(), parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
		UploadPhone up = getList().get(position);
		logE(String.valueOf(position), up.getTelephone() + "--" + up.getName() + "--" + up.getSortLetters() + "--" + up.isUploaded());
		String name = up.getName();
		holder.bd.tvFirstName.setText(name.substring(name.length() - 1));
		holder.bd.tvName.setText(up.getName());
		super.setOnItemClickListener(holder.itemView, position);
	}

    public class ViewHolder extends RecyclerView.ViewHolder {
		AdapterAddressBinding bd;
		ViewHolder(AdapterAddressBinding bd) {
            super(bd.getRoot());
			this.bd = bd;
		}
	}
}
