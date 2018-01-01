package example.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
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
        AdapterFriendBinding bd = DataBindingUtil.inflate(mInflater, R.layout.adapter_friend, parent, false);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterFriendBinding bd;

        ViewHolder(AdapterFriendBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }
}
