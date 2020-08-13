package example.drag;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.ViewGroup;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.DragAdapterBinding;

import java.util.ArrayList;

public class DragAdapter extends BaseRecyclerViewAdapter<Integer> {


    public DragAdapter(Context context, ArrayList<Integer> dataList) {
        super(context, dataList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        DragAdapterBinding bd = getBinding(R.layout.drag_adapter, parent);
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
        DragAdapterBinding bd;

        ViewHolder(DragAdapterBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }
}
