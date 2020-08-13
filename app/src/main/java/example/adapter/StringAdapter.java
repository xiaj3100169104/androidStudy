package example.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.AdapterFriendBinding;

import java.util.ArrayList;

public class StringAdapter extends BaseRecyclerViewAdapter<String> {
    public StringAdapter(Context context, ArrayList<String> list) {
        super(context, list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterFriendBinding bd = getBinding(R.layout.adapter_friend, parent);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        String f = getData(position);
        holder.bd.viewMark.setText(f);
        super.setOnItemClickListener(holder.itemView, position);
        holder.bd.executePendingBindings();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder {
        private AdapterFriendBinding bd;

        ViewHolder(AdapterFriendBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }
}
