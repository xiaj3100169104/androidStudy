package example.music;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.framework.databinding.AdapterMusicBinding;

import java.util.ArrayList;

import example.music.entity.MediaBean;

public class AudioAdapter extends BaseRecyclerViewAdapter<MediaBean> {
    public AudioAdapter(Context context, ArrayList<MediaBean> list) {
        super(context, list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterMusicBinding bd = getBinding(R.layout.adapter_music, parent);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        MediaBean f = (MediaBean) getData(position);
        holder.bd.name.setText(f.name);
        super.setOnItemClickListener(holder.itemView, position);
        holder.bd.executePendingBindings();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterMusicBinding bd;

        ViewHolder(AdapterMusicBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }
}
