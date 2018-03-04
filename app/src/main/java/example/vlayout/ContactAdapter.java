package example.vlayout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;
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
public class ContactAdapter extends BaseDelegateAdapter<String> {

    public static final int TYPE_CONTACT = 2;
    private final LinearLayoutHelper helper;

    public ContactAdapter(Context context, ArrayList<String> dataList, LinearLayoutHelper helper) {
        super(context, dataList);
        this.helper = helper;

    }

    /**
     * 必须重写不然会出现滑动不流畅的情况
     */
    @Override
    public int getItemViewType(int position) {
        return TYPE_CONTACT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderBannerBinding bd = DataBindingUtil.inflate(mInflater, R.layout.holder_banner, parent, false);
        return new ContactViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

        ContactViewHolder holder = (ContactViewHolder) viewHolder;
        String up = getData(position);
        holder.bd.catalog.setText(up);
        holder.bd.executePendingBindings();

    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return helper;
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        HolderBannerBinding bd;

        ContactViewHolder(HolderBannerBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }

}