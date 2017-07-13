package com.style.lib.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiajun.libalbumselect.R;

import java.util.List;


public class ImageItemAdapter extends BaseRecyclerViewAdapter<ImageItem> {

    public ImageItemAdapter(Context context, List<ImageItem> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.adapter_album_image_item, parent, false);
        ViewHolder holder = new ViewHolder(v);
        holder.image = (ImageView) v.findViewById(R.id.image);
        holder.isselected = (ImageView) v.findViewById(R.id.isselected);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        ImageItem item = getData(position);
        String path = item.getImagePath();
        ImageLoadManager.loadNormalPicture(mContext, ((ViewHolder) viewHolder).image, path);

        if (item.isSelected()) {
            holder.isselected.setVisibility(View.VISIBLE);
        } else {
            holder.isselected.setVisibility(View.INVISIBLE);
        }
        super.setOnItemClickListener(holder, position);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        ImageView isselected;
        ViewHolder(View view) {
            super(view);
        }
    }
}
