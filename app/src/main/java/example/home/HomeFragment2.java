package example.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.style.base.BaseFragment;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.bean.Friend;
import com.style.bean.User;
import com.style.db.user.UserDBManager;
import com.style.framework.R;
import com.style.manager.AccountManager;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;


public class HomeFragment2 extends BaseFragment {
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private List<Friend> dataList;
    private LinearLayoutManager layoutManager;
    private FriendAdapter adapter;

    private UserDBManager myTableManager;
    private User curUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mLayoutResID = R.layout.fragment_home_2;
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initData() {
        curUser = AccountManager.getInstance().getCurrentUser();
        myTableManager = UserDBManager.getInstance();

        dataList = new ArrayList<>();
        adapter = new FriendAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {

            }
        });
        getData();
    }

    private void getData() {
        List<Friend> list = myTableManager.getAllFriend(curUser.getUserId());
        if (list != null) {
            dataList.clear();
            dataList.addAll(list);
            adapter.notifyDataSetChanged();
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        myTableManager.closeDB();
    }
}
