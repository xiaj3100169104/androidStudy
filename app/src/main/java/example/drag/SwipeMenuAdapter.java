package example.drag;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.dmcbig.mediapicker.utils.ScreenUtils;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.DragAdapterBinding;
import com.style.framework.databinding.SwipeMenuAdapterBinding;

import java.util.ArrayList;

public class SwipeMenuAdapter extends BaseRecyclerViewAdapter<String> {


    private final int screenWidth;

    public SwipeMenuAdapter(Context context, ArrayList<String> dataList) {
        super(context, dataList);
        screenWidth = ScreenUtils.getScreenWidth(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SwipeMenuAdapterBinding bd = getBinding(R.layout.swipe_menu_adapter, parent);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        String f = getData(position);
        holder.bd.viewMark.setText(f);
        //holder.bd.viewNick.setText(f.getUser().getUserName());
        //super.setOnItemClickListener(holder.itemView, position);
        holder.bd.layoutFore.getLayoutParams().width = screenWidth;
        holder.bd.layoutFore.setOnClickListener(v -> {
            if (getOnItemClickListener() != null) {
                getOnItemClickListener().onItemClick(position, getData(position));
            }
        });
        holder.bd.viewEdit.setOnClickListener(v -> {
            logE("SwipeMenuAdapter", position + "  编辑");
        });
        holder.bd.viewDelete.setOnClickListener(v -> {
            logE("SwipeMenuAdapter", position + "  删除");
        });
        holder.bd.executePendingBindings();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SwipeMenuAdapterBinding bd;

        ViewHolder(SwipeMenuAdapterBinding bd) {
            super(bd.getRoot());
            this.bd = bd;
        }
    }
}
