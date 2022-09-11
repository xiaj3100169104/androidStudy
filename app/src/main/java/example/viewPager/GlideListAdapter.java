package example.viewPager;

import android.app.Activity;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.dmcbig.mediapicker.entity.Media;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.AdapterGlideListBinding;
import com.style.data.glide.ImageLoader;

import java.util.ArrayList;

public class GlideListAdapter extends BaseRecyclerViewAdapter<Media> {
    public GlideListAdapter(Context context, ArrayList<Media> list) {
        super(context, list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterGlideListBinding bd = AdapterGlideListBinding.inflate(getLayoutInflater(), parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        Media f = (Media) getList().get(position);
        //jvm能申请的最大内存
        Log.e("maxMemory", Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M");
        //jvm已经申请到的内存
        Log.e("totalMemory", Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M");
        //jvm剩余空闲内存
        Log.e("freeMemory", Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M");
        //Glide.get(getContext()).clearMemory();
        ImageLoader.load((Activity) getContext(), f.path, holder.bd.iv);
        super.setOnItemClickListener(holder.itemView, position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterGlideListBinding bd;

        ViewHolder(AdapterGlideListBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }
}
