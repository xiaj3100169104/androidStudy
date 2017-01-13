package test.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.framework.R;
import com.style.utils.MyDateUtil;
import com.style.view.CustomNotifyView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import test.im.MsgItem;

public class MsgListAdapter extends BaseRecyclerViewAdapter {

    public MsgListAdapter(Context context, List list) {
        super(context, list);
    }


    @Override
    public RecyclerView.ViewHolder onCreateItem(ViewGroup parent, int viewType) {
        return new ViewHolder(mInflater.inflate(R.layout.adapter_msg, parent, false));
    }

    @Override
    public void onBindItem(RecyclerView.ViewHolder viewHolder, int position, Object data) {
        ViewHolder holder = (ViewHolder) viewHolder;
        MsgItem item = (MsgItem) data;
        setText(holder.viewMark, item.getFriend().getMark());
        setText(holder.viewTime, MyDateUtil.getTimeConversationString(item.getMsg().getCreateTime()));
        setText(holder.viewContent, item.getMsg().getContent());
        int count = item.getUnreadCount();
        holder.viewUnread.setNotifyCount(count);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.view_mark)
        TextView viewMark;
        @Bind(R.id.view_time)
        TextView viewTime;
        @Bind(R.id.view_content)
        TextView viewContent;
        @Bind(R.id.view_unread)
        CustomNotifyView viewUnread;

        ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
