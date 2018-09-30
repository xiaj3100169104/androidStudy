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

public class DynamicPublishImageAdapter : BaseRecyclerViewAdapter<Media> {

    private var listener: OnDeleteClickListener? = null;

    constructor(context: Context, list: ArrayList<Media>) : super(context, list)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var bd: AdapterPublishDynamicPictureBinding = getBinding(R.layout.adapter_publish_dynamic_picture, parent);
        return ViewHolder(bd);
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val index = position;
        var holder: ViewHolder = holder as ViewHolder
        var media = getData(position);
        if (position != getItemCount() - 1) {
            holder.bd.ivDelete.setVisibility(View.VISIBLE);
            ImageLoader.loadNormalPicture(getContext() as Activity, holder.bd.ivActiveImages, media.path);
        } else {
            holder.bd.ivDelete.setVisibility(View.GONE);
            holder.bd.ivActiveImages.setImageResource(R.mipmap.ic_add_photo);
        }
        super.setOnItemClickListener(holder.itemView, position);

        holder.bd.ivDelete.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                listener?.onItemClickDelete(index);
            }
        });
        holder.bd.executePendingBindings();
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var bd: AdapterPublishDynamicPictureBinding

        constructor(bd: AdapterPublishDynamicPictureBinding) : super(bd.root) {
            this.bd = bd;
        }
    }

    fun setOnDeleteClickListener(listener: OnDeleteClickListener) {
            this.listener = listener;
    }

    public interface OnDeleteClickListener {
        fun onItemClickDelete(position: Int);
    }

}