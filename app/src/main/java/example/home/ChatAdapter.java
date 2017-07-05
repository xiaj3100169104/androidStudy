package example.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.style.base.BaseRecyclerViewAdapter;
import com.style.bean.User;
import com.style.framework.R;
import com.style.manager.AccountManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import xj.mqtt.bean.IMMessage;
import xj.mqtt.bean.TextMsg;

public class ChatAdapter extends BaseRecyclerViewAdapter<IMMessage> {
    private static final int MSG_TYPE_TEXT_LEFT = 0;
    private static final int MSG_TYPE_TEXT_RIGHT = 1;


    private User curUser;

    public ChatAdapter(Context context, List<IMMessage> list) {
        super(context, list);
        curUser = AccountManager.getInstance().getCurrentUser();
    }

    @Override
    public int getItemViewType(int position) {
        IMMessage msg = getData(position);
        if (msg.getSenderId().equals(curUser.getUserId()))
            return MSG_TYPE_TEXT_RIGHT;
        else
            return MSG_TYPE_TEXT_LEFT;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_TEXT_LEFT)
            return new ViewHolderTextLeft(mInflater.inflate(R.layout.adapter_msg_text_left, parent, false));
        else
            return new ViewHolderTextRight(mInflater.inflate(R.layout.adapter_msg_text_right, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        IMMessage msg = getData(position);
        if (getItemViewType(position) == MSG_TYPE_TEXT_LEFT) {
            ViewHolderTextLeft holder = (ViewHolderTextLeft) viewHolder;
            TextMsg textMsg = (TextMsg) msg.getBodyObj();
            setText(holder.viewContent, textMsg.content);
            super.setOnItemClickListener(holder, position);

        }else {
            ViewHolderTextRight holder = (ViewHolderTextRight) viewHolder;
            TextMsg textMsg = (TextMsg) msg.getBodyObj();
            setText(holder.viewContent, textMsg.content);
            super.setOnItemClickListener(holder, position);

        }

    }

    public class ViewHolderTextLeft extends RecyclerView.ViewHolder {
        @Bind(R.id.view_avatar)
        ImageView viewAvatar;
        @Bind(R.id.view_content)
        TextView viewContent;

        ViewHolderTextLeft(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    public class ViewHolderTextRight extends RecyclerView.ViewHolder {
        @Bind(R.id.view_avatar)
        ImageView viewAvatar;
        @Bind(R.id.view_content)
        TextView viewContent;

        ViewHolderTextRight(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
