package example.album;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dmcbig.mediapicker.entity.Media;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.AdapterPublishDynamicPictureBinding;
import com.style.data.glide.ImageLoader;

import java.util.ArrayList;

public class DynamicPublishImageAdapter extends BaseRecyclerViewAdapter<Media> {

    private OnDeleteClickListener listener;

    public DynamicPublishImageAdapter(Context context, ArrayList<Media> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterPublishDynamicPictureBinding bd = getBinding(R.layout.adapter_publish_dynamic_picture, parent);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final int index = position;
        ViewHolder holder = (ViewHolder) viewHolder;
        Media media = getData(position);
        if (position != getItemCount() - 1) {
            holder.bd.ivDelete.setVisibility(View.VISIBLE);
            ImageLoader.loadNormalPicture((Activity) getContext(), holder.bd.ivActiveImages, media.path);

        } else {
            holder.bd.ivDelete.setVisibility(View.GONE);
            holder.bd.ivActiveImages.setImageResource(R.mipmap.ic_add_photo);
        }
        super.setOnItemClickListener(holder.itemView, position);

        holder.bd.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClickDelete(index);
                }
            }
        });
        holder.bd.executePendingBindings();

    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        AdapterPublishDynamicPictureBinding bd;

        ViewHolder(AdapterPublishDynamicPictureBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }

    public interface OnDeleteClickListener {
        void onItemClickDelete(int position);
    }

}