package example.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.bean.Friend;
import com.style.framework.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class StringAdapter extends BaseRecyclerViewAdapter<String> {
    public StringAdapter(Context context, ArrayList<String> list) {
        super(context, list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.adapter_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        ViewHolder holder = (ViewHolder) viewHolder;
        String f = getData(position);
        setText(holder.tv_mark, f);
        super.setOnItemClickListener(holder, position);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.view_mark)
        TextView tv_mark;
        @Bind(R.id.view_nick)
        TextView tv_name;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
