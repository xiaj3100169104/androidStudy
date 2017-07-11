package cn.style.album;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.style.manager.ImageLoadManager;


public class ImageItemAdapter extends BaseRecyclerViewAdapter {

    public ImageItemAdapter(Context context, List list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.adapter_album_image_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        ImageItem item = (ImageItem) getData(position);
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
        @Bind(R.id.image)
        ImageView image;
        @Bind(R.id.isselected)
        ImageView isselected;


        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
