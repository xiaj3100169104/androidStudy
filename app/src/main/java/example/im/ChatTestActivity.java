package example.im;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.EditText;

import com.style.base.BaseToolBarActivity;
import com.style.bean.Friend;
import com.style.bean.User;
import com.style.framework.R;
import com.style.manager.AccountManager;
import com.style.view.DividerItemDecoration;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import example.home.ChatAdapter;
import xj.mqtt.bean.IMMessage;
import xj.mqtt.bean.MsgAction;
import xj.mqtt.db.MsgDBManager;
import xj.mqtt.manager.IMSend;


public class ChatTestActivity extends BaseToolBarActivity {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.et_content)
    EditText etContent;
    private User curUser;
    private Friend friend;
    private List<IMMessage> dataList;
    private LinearLayoutManager layoutManager;
    private ChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mLayoutResID = R.layout.activity_chat_test;
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        curUser = AccountManager.getInstance().getCurrentUser();
        friend = (Friend) getIntent().getSerializableExtra("friend");
        setToolbarTitle(friend.getMark());

        MsgDBManager.getInstance().updateReaded2User(friend.getFriendId(), friend.getOwnerId());
        dataList = new ArrayList<>();
        adapter = new ChatAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setAdapter(adapter);
        getData();
    }

    private void getData() {
        getPageLoad();
        recyclerView.smoothScrollToPosition(dataList.size());
    }

   /* @OnClick(R.id.view_clear)
    public void getClear() {
        MsgDBManager.getInstance().deleteMsg(curUser.getUserId(), 1);
    }*/

    @OnClick(R.id.view_page_load)
    public void getPageLoad() {
        int startIndex = dataList.size();//100;
        List<IMMessage> list = MsgDBManager.getInstance().getMsgByPage(curUser.getUserId(), friend.getFriendId(), startIndex, 20);
        if (list != null) {
            dataList.addAll(0, list);
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.view_send)
    public void send() {
        String content = etContent.getText().toString();
        if (!TextUtils.isEmpty(content))
            IMSend.sendText(friend.friendId, content);
    }

    //在UI线程中执行
    @Subscriber(tag = MsgAction.MSG_NEW)
    public void onNewMsg(IMMessage iMsg) {
        //不是我发送的消息状态置为已读
        if (isFriendSend(iMsg) || isMyselfSend(iMsg)) {
            if (isFriendSend(iMsg))
                MsgDBManager.getInstance().update2Readed(iMsg.getMsgId());
            dataList.add(iMsg);
            adapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(dataList.size());
        }

    }

    private boolean isFriendSend(IMMessage iMsg) {
        //先判断消息是否和当前联系人有关，可以提高效率
        if (iMsg.getSenderId() == friend.getFriendId() && iMsg.getReceiverId() == curUser.getUserId())
            return true;
        return false;
    }

    private boolean isMyselfSend(IMMessage iMsg) {
        if (iMsg.getReceiverId() == friend.getFriendId() && iMsg.getSenderId() == curUser.getUserId())
            return true;
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }
}