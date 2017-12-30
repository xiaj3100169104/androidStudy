package com.style.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

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
public class MultiTypeRecyclerViewAdapter extends BaseRecyclerViewAdapter<UploadPhone> {
    public static final int TYPE_BANNER = 0;
    public static final int TYPE_HEADER = 1;
    public static final int TYPE_CONTACT = 2;
    public static final int TYPE_ADDRESS = 3;

    public MultiTypeRecyclerViewAdapter(Context context, ArrayList<UploadPhone> dataList) {
        super(context, dataList);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_BANNER;
        else if (position == 1)
            return TYPE_ADDRESS;
        else if (position == 2)
            return TYPE_HEADER;
        return TYPE_CONTACT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) {
            HolderBannerBinding bd = DataBindingUtil.inflate(mInflater, R.layout.holder_banner, parent, false);
            return new BannerViewHolder(bd);
        } else if (viewType == TYPE_HEADER) {
            HolderHeaderBinding bd = DataBindingUtil.inflate(mInflater, R.layout.holder_header, parent, false);
            return new HeaderViewHolder(bd);
        } else if (viewType == TYPE_ADDRESS) {
            HolderAddressBinding bd = DataBindingUtil.inflate(mInflater, R.layout.holder_address, parent, false);
            return new AddressViewHolder(bd);
        }
        AdapterAddressBinding bd = DataBindingUtil.inflate(mInflater, R.layout.adapter_address, parent, false);
        return new ContactViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {
            case TYPE_BANNER:
                break;
            case TYPE_ADDRESS:
                AddressViewHolder holder2 = (AddressViewHolder) viewHolder;
                ArrayList<UploadPhone> list2 = new ArrayList();
                /*if (list.size() > 4)
                    list2 = (ArrayList<UploadPhone>) getList().subList(3, getItemCount() - 3);*/
                UploadPhoneAdapter adapter = new UploadPhoneAdapter(getContext(), list2);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                holder2.bd.recyclerView.setLayoutManager(layoutManager);
                holder2.bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
                holder2.bd.recyclerView.setAdapter(adapter);
                holder2.bd.executePendingBindings();
                break;
            case TYPE_HEADER:

                break;
            case TYPE_CONTACT:
                ContactViewHolder holder = (ContactViewHolder) viewHolder;
                UploadPhone up = getData(position);
                logE(String.valueOf(position), up.getTelephone() + "--" + up.getName() + "--" + up.getSortLetters() + "--" + up.isUploaded());
                String name = up.getName();
                holder.bd.tvFirstName.setText(name.substring(name.length() - 1, name.length()));
                holder.bd.tvName.setText(up.getName());
                holder.bd.catalog.setText(up.getSortLetters());
                holder.bd.executePendingBindings();
                break;
        }

    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        HolderBannerBinding bd;
        BannerViewHolder(HolderBannerBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        HolderHeaderBinding bd;
        HeaderViewHolder(HolderHeaderBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        HolderAddressBinding bd;
        AddressViewHolder(HolderAddressBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        AdapterAddressBinding bd;
        ContactViewHolder(AdapterAddressBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }
    /*  @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemViewType(position) == TYPE_HEADER
                            ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            if (holder.getLayoutPosition() == 0 && holder.getItemViewType() == TYPE_HEADER) {
                p.setFullSpan(true);
            } else {
                p.setFullSpan(false);
            }
        }
    }*/
}