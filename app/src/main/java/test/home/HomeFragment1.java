package test.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseFragment;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.bean.Friend;
import com.style.bean.IMsg;
import com.style.bean.User;
import com.style.db.msg.MsgDBManager;
import com.style.db.user.UserDBManager;
import com.style.framework.R;
import com.style.manager.AccountManager;
import com.style.view.DividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import test.im.ChatTestActivity;
import test.im.MsgItem;


public class HomeFragment1 extends BaseFragment {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;

    private UserDBManager myTableManager;
    private User curUser;
    private List<MsgItem> dataList;
    private LinearLayoutManager layoutManager;
    private MsgListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_home_1;
        EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
        curUser = AccountManager.getInstance().getCurrentUser();
        myTableManager = UserDBManager.getInstance();

        dataList = new ArrayList<>();
        adapter = new MsgListAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                MsgItem item = (MsgItem) data;
                Intent i = new Intent(getContext(), ChatTestActivity.class);
                startActivity(i.putExtra("friend", item.getFriend()));
            }
        });
        getData();
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {
        List<Friend> list = myTableManager.getAllFriend(curUser.getUserId());
        if (list != null && list.size() > 0) {
            List<MsgItem> msgItems = new ArrayList<>();
            for (Friend f : list) {
                IMsg last = MsgDBManager.getInstance().getLastMessageWithEveryFriend(f.getOwnerId(), f.getFriendId());
                if (last != null) {
                    MsgItem msgItem = new MsgItem();
                    msgItem.setFriend(f);
                    msgItem.setMsg(last);
                    msgItem.setUnreadCount(MsgDBManager.getInstance().getUnreadCount(f.getOwnerId(), f.getFriendId()));
                    msgItems.add(msgItem);
                }
            }
            if (msgItems != null && msgItems.size() > 0) {
                dataList.clear();
                dataList.addAll(msgItems);
                adapter.notifyDataSetChanged();
            }
        }

    }

    //在UI线程中执行
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNewMsg(IMsg iMsg) {
        Log.e(TAG, "onNewMsg");
        if (iMsg != null) {
            for (MsgItem msgItem : dataList) {
                Friend f = msgItem.getFriend();
                if (iMsg.getSenderId() == f.getFriendId() && iMsg.getReceiverId() == f.getOwnerId()){
                    msgItem.setUnreadCount(MsgDBManager.getInstance().getUnreadCount(f.getOwnerId(), f.getFriendId()));
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //取消事件注册
        EventBus.getDefault().unregister(this);
    }

}