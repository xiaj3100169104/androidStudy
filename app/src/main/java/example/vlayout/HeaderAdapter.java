package example.vlayout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.GridLayoutHelper;
import com.style.framework.R;
import com.style.framework.databinding.HolderHeaderBinding;

import java.util.ArrayList;

/**
 * Created by XiaJun on 2015/7/2.
 */
public class HeaderAdapter extends BaseDelegateAdapter<String> {
    public static final int TYPE_HEADER = 1;
    private final GridLayoutHelper helper;

    public HeaderAdapter(Context context, ArrayList<String> dataList, GridLayoutHelper helper) {
        super(context, dataList);
        this.helper = helper;
    }

    /**
     * 必须重写不然会出现滑动不流畅的情况
     */
    @Override
    public int getItemViewType(int position) {
        return TYPE_HEADER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderHeaderBinding bd = DataBindingUtil.inflate(mInflater, R.layout.holder_header, parent, false);
        return new HeaderViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        HeaderViewHolder holder = (HeaderViewHolder) viewHolder;
        String up = getData(position);
        holder.bd.catalog.setText(up);
        holder.bd.executePendingBindings();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return helper;
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        HolderHeaderBinding bd;

        HeaderViewHolder(HolderHeaderBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }

}