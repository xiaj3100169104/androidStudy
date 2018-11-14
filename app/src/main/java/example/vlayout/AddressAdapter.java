package example.vlayout;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.alibaba.android.vlayout.LayoutHelper;
import com.style.framework.R;
import com.style.framework.databinding.HolderAddressBinding;
import com.style.view.systemHelper.DividerItemDecoration;

import java.util.ArrayList;

import example.address.UploadPhone;
import example.address.UploadPhoneAdapter;

/**
 * Created by XiaJun on 2015/7/2.
 */
public class AddressAdapter extends BaseDelegateAdapter {

    public static final int TYPE_ADDRESS = 3;

    public AddressAdapter(Context context, ArrayList<UploadPhone> dataList) {
        super(context, dataList);
    }

    /**
     * 必须重写不然会出现滑动不流畅的情况
     */
    @Override
    public int getItemViewType(int position) {
        return TYPE_ADDRESS;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        HolderAddressBinding bd = DataBindingUtil.inflate(mInflater, R.layout.holder_address, parent, false);
        return new AddressViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        AddressViewHolder holder2 = (AddressViewHolder) viewHolder;

        UploadPhoneAdapter adapter = new UploadPhoneAdapter(getContext(), list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        holder2.bd.recyclerView.setLayoutManager(layoutManager);
        holder2.bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        holder2.bd.recyclerView.setAdapter(adapter);
        holder2.bd.executePendingBindings();
    }

    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return null;
    }


    public class AddressViewHolder extends RecyclerView.ViewHolder {
        HolderAddressBinding bd;

        AddressViewHolder(HolderAddressBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }

}