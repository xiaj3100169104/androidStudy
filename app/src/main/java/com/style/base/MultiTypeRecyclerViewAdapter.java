package com.style.base;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import example.address.UploadPhone;
import example.address.UploadPhoneAdapter;
import com.style.framework.R;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

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
        if (viewType == TYPE_BANNER)
            return new BannerViewHolder(mInflater.inflate(R.layout.holder_banner, parent, false));
        else if (viewType == TYPE_HEADER)
            return new HeaderViewHolder(mInflater.inflate(R.layout.holder_header, parent, false));
        else if (viewType == TYPE_ADDRESS)
            return new AddressViewHolder(mInflater.inflate(R.layout.holder_address, parent, false));
        return new ContactViewHolder(mInflater.inflate(R.layout.adapter_address, parent, false));
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
                if (list.size() > 4)
                    list2 = (ArrayList<UploadPhone>) getList().subList(3, getItemCount() - 3);
                UploadPhoneAdapter adapter = new UploadPhoneAdapter(getContext(), list2);
                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
                layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
                holder2.recyclerView.setLayoutManager(layoutManager);
                holder2.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
                holder2.recyclerView.setAdapter(adapter);
                break;
            case TYPE_HEADER:
                break;
            case TYPE_CONTACT:
                ContactViewHolder holder = (ContactViewHolder) viewHolder;
                UploadPhone up = getData(position);
                logE(String.valueOf(position), up.getTelephone() + "--" + up.getName() + "--" + up.getSortLetters() + "--" + up.isUploaded());
                String name = up.getName();
                holder.tv_first_name.setText(name.substring(name.length() - 1, name.length()));
                holder.tv_name.setText(up.getName());
                break;
        }

    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        public BannerViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class AddressViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.recyclerView)
        RecyclerView recyclerView;

        AddressViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_first_name)
        TextView tv_first_name;
        @Bind(R.id.tv_name)
        TextView tv_name;
        @Bind(R.id.catalog)
        TextView tvLetter;
        @Bind(R.id.tv_add)
        TextView tv_add;

        ContactViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
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