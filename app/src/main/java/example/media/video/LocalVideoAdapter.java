package example.media.video;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.constant.ConfigUtil;
import com.style.framework.R;
import com.style.framework.databinding.AdapterLocalVideoBinding;
import com.style.utils.BitmapUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class LocalVideoAdapter extends BaseRecyclerViewAdapter<File> {
    public LocalVideoAdapter(Context context, ArrayList<File> list) {
        super(context, list);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterLocalVideoBinding bd = DataBindingUtil.inflate(mInflater, R.layout.adapter_local_video, parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        File f = getData(position);
        holder.bd.tvName.setText(f.getName());

        setImageResource(f, holder.bd.ivVideoPreview);
        super.setOnItemClickListener(holder.itemView, position);
        holder.bd.executePendingBindings();
    }

    private void setImageResource(File f, ImageView imageView) {
        String path = ConfigUtil.DIR_CACHE + "/" + f.getName() + ".thumbnail";

        Bitmap b = BitmapUtil.revitionImageSize(path, 1080, 640, 640);
        if (b == null) {
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(f.getAbsolutePath());
            b = retriever.getFrameAtTime(1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST);
            try {
                BitmapUtil.saveBitmap(path, b, 100);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        imageView.setImageBitmap(b);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterLocalVideoBinding bd;

        ViewHolder(AdapterLocalVideoBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }
}
