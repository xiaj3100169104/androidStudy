package test.im;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.style.base.BaseToolBarActivity;
import com.style.bean.Friend;
import com.style.bean.IMsg;
import com.style.db.msg.MsgDBManager;
import com.style.framework.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import test.home.ChatAdapter;

import com.style.bean.User;
import com.style.manager.AccountManager;
import com.style.utils.MyDateUtil;
import com.style.view.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ChatTestActivity extends BaseToolBarActivity {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private User curUser;
    private Friend friend;
    private List<IMsg> dataList;
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
       /* if (dataList.size() > 0)
            startIndex = (int) dataList.get(dataList.size() - 1).getId();*/
        List<IMsg> list = MsgDBManager.getInstance().getMsgByPage(curUser.getUserId(), friend.getFriendId(), startIndex, 20);
        if (list != null) {
            dataList.addAll(0, list);
            adapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.view_send)
    public void send() {
        IMsg o = new IMsg();
        o.setMsgId(System.currentTimeMillis());
        o.setSenderId(curUser.getUserId());
        o.setReceiverId(friend.friendId);
        o.setState(1);
        o.setCreateTime(System.currentTimeMillis());
        //o.setContent(curUser.getUserName() + ":" + MyDateUtil.longToString(System.currentTimeMillis(), MyDateUtil.FORMAT_yyyy_MM_dd_HH_mm_ss));
        MsgDBManager.getInstance().insertMsg(o);

    }

    //在UI线程中执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMsg(IMsg iMsg) {
        Log.e("MainThread", Thread.currentThread().getName());
        //不是我发送的消息状态置为已读
        if (isFriendSend(iMsg) || isMyselfSend(iMsg)) {
            if (isFriendSend(iMsg))
                MsgDBManager.getInstance().updateReaded2Msg(curUser.getUserId());
            dataList.add(iMsg);
            adapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(dataList.size());
        }

    }

    private boolean isFriendSend(IMsg iMsg) {
        //先判断消息是否和当前联系人有关，可以提高效率
        if (iMsg.getSenderId() == friend.getFriendId() && iMsg.getReceiverId() == curUser.getUserId())
            return true;
        return false;
    }

    private boolean isMyselfSend(IMsg iMsg) {
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