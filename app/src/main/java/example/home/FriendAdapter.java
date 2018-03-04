package example.home;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.bean.Friend;
import com.style.framework.R;
import com.style.framework.databinding.AdapterFriendBinding;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class FriendAdapter extends BaseRecyclerViewAdapter<Integer> {
    public FriendAdapter(Context context, ArrayList<Integer> list) {
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
        Integer f = getData(position);
        holder.bd.viewMark.setText("数据" + f);
        //holder.bd.viewNick.setText(f.getUser().getUserName());
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
