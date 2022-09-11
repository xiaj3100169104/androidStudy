package example.music;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
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
        AdapterMusicBinding bd = AdapterMusicBinding.inflate(getLayoutInflater(), parent, false);
        return new ViewHolder(bd);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        MediaBean f = (MediaBean) getList().get(position);
        holder.bd.name.setText(f.name);
        super.setOnItemClickListener(holder.itemView, position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AdapterMusicBinding bd;

        ViewHolder(AdapterMusicBinding bd) {
            super(bd.getRoot());
            this.bd = bd;

        }
    }
}
