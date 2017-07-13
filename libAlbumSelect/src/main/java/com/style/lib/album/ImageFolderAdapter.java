package com.style.lib.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.xiajun.libalbumselect.R;

import java.util.List;

public class ImageFolderAdapter extends BaseRecyclerViewAdapter<PicBucket> {

    public ImageFolderAdapter(Context context, List<PicBucket> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = mInflater.inflate(R.layout.adapter_album_image_bucket, parent, false);
        ViewHolder holder = new ViewHolder(v);
        holder.image = (ImageView) v.findViewById(R.id.image);
        holder.tvName = (TextView) v.findViewById(R.id.tv_name);
        holder.tvCount = (TextView) v.findViewById(R.id.tv_count);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        PicBucket item = getData(position);
        List<ImageItem> items = item.getImages();
        int num = 0;
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).isSelected()) {
                num++;
            }
        }
        String path = item.getImages().get(0).getImagePath();
        ImageLoadManager.loadNormalPicture(mContext, holder.image, path);
        holder.tvName.setText(item.getBucketName());
        holder.tvCount.setText(" (" + num + "/" + item.getImages().size() + ")");
        super.setOnItemClickListener(holder, position);

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView tvCount;
        TextView tvName;
        ViewHolder(View view) {
            super(view);
        }
    }
}
