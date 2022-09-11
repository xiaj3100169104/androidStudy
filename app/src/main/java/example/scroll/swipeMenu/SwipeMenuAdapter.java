package example.scroll.swipeMenu;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.TypedValue;
import android.view.ViewGroup;

import com.dmcbig.mediapicker.utils.ScreenUtils;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.databinding.SwipeMenuAdapterBinding;

import java.util.ArrayList;

public class SwipeMenuAdapter extends BaseRecyclerViewAdapter<String> {
    private final int screenWidth;
    private final int width;
    private final SwipeMenuRecyclerView recyclerView;
    private OnSwipeMenuListener listener;

    public SwipeMenuAdapter(Context context, ArrayList<String> dataList, SwipeMenuRecyclerView recyclerView) {
        super(context, dataList);
        this.recyclerView = recyclerView;
        screenWidth = ScreenUtils.getScreenWidth(context);
        int menuWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, context.getResources().getDisplayMetrics());
        width = screenWidth + menuWidth;
        this.recyclerView.setMenuMaxOffset(menuWidth);
    }

    public void notifyDataChanged() {
        this.recyclerView.closeMenuFromOpen();
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        SwipeMenuAdapterBinding bd = SwipeMenuAdapterBinding.inflate(getLayoutInflater(), parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        String f = getList().get(position);
        holder.bd.viewMark.setText(f);
        //holder.bd.viewNick.setText(f.getUser().getUserName());
        //super.setOnItemClickListener(holder.itemView, position);
        holder.bd.layoutRoot.getLayoutParams().width = width;
        holder.bd.layoutFore.getLayoutParams().width = screenWidth;
        holder.bd.layoutFore.setOnClickListener(v -> {
            if (getOnItemClickListener() != null) {
                getOnItemClickListener().onItemClick(position, f);
            }
        });
        holder.bd.viewEdit.setOnClickListener(v -> {
            logE("SwipeMenuAdapter", position + "  编辑");
            if (listener != null) {
                listener.onMenuEdit(position, f);
            }
        });
        holder.bd.viewDelete.setOnClickListener(v -> {
            logE("SwipeMenuAdapter", position + "  删除");
            if (listener != null)
                listener.onMenuDelete(position, f);
        });
        holder.setIsRecyclable(false);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SwipeMenuAdapterBinding bd;

        ViewHolder(SwipeMenuAdapterBinding bd) {
            super(bd.getRoot());
            this.bd = bd;
        }
    }

    public void setOnSwipeMenuListener(OnSwipeMenuListener<String> listener) {
        this.listener = listener;
    }

    public interface OnSwipeMenuListener<String> {

        void onMenuEdit(int position, String data);

        void onMenuDelete(int position, String data);
    }
}
