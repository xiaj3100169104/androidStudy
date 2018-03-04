package example.vlayout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.SingleLayoutHelper;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.AdapterAddressBinding;
import com.style.framework.databinding.HolderAddressBinding;
import com.style.framework.databinding.HolderBannerBinding;
import com.style.framework.databinding.HolderHeaderBinding;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;

import example.address.UploadPhone;
import example.address.UploadPhoneAdapter;

/**
 * Created by XiaJun on 2015/7/2.
 */
public class BannerAdapter extends BaseDelegateAdapter<String> {
    public static final int TYPE_BANNER = 0;
    private final SingleLayoutHelper helper;

    public BannerAdapter(Context context, ArrayList<String> dataList, SingleLayoutHelper helper) {
        super(context, dataList);
        this.helper = helper;
    }

    /**
     * 必须重写不然会出现滑动不流畅的情况
     */
    @Override
    public int getItemViewType(int position) {
        return TYPE_BANNER;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderBannerBinding bd = DataBindingUtil.inflate(mInflater, R.layout.holder_banner, parent, false);
        return new BannerViewHolder(bd);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        BannerViewHolder holder = (BannerViewHolder) viewHolder;
        String up = getData(position);
        holder.bd.catalog.setText(up);
        holder.bd.executePendingBindings();

    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return helper;
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        HolderBannerBinding bd;

        BannerViewHolder(HolderBannerBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }

}