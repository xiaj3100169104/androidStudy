package example.home;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.style.base.BaseFragment;
import com.style.base.BaseRecyclerViewAdapter;
import com.style.bean.User;
import com.style.db.user.UserDBManager;
import com.style.framework.R;
import com.style.framework.databinding.FragmentHome2Binding;
import com.style.app.AccountManager;
import com.style.view.DividerItemDecoration;

import java.util.ArrayList;


public class HomeFragment2 extends BaseFragment {
    FragmentHome2Binding bd;
    private ArrayList<Integer> dataList;
    private LinearLayoutManager layoutManager;
    private FriendAdapter adapter;

    private UserDBManager myTableManager;
    private User curUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        bd = DataBindingUtil.inflate(inflater, R.layout.fragment_home_2, container, false);
        return bd.getRoot();

    }

    @Override
    protected void initData() {
        curUser = AccountManager.getInstance().getCurrentUser();
        myTableManager = UserDBManager.getInstance();

        dataList = new ArrayList<>();
        adapter = new FriendAdapter(getContext(), dataList);
        layoutManager = new LinearLayoutManager(getContext());
        bd.recyclerView.setLayoutManager(layoutManager);
        bd.recyclerView.addItemDecoration(new DividerItemDecoration(getContext()));
        bd.recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new BaseRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, Object data) {
                showToast(position + "");
            }
        });
        bd.refreshLayout.setEnableAutoLoadMore(true);//开启自动加载功能（非必须）
        bd.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refresh();

                    }
                }, 2000);
            }
        });
        bd.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (adapter.getItemCount() > 20) {
                            Toast.makeText(getContext(), "数据全部加载完毕", Toast.LENGTH_SHORT).show();
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        } else {
                            loadMore();
                        }
                    }
                }, 2000);
            }
        });

        //触发自动刷新
        bd.refreshLayout.autoRefresh();
        //getData();
    }

    private void loadMore() {
        int k = dataList.size();
        for (int i = k; i < k + 5; i++) {
            dataList.add(i);
        }
        adapter.notifyDataSetChanged();

        bd.refreshLayout.finishLoadMore();
    }

    private void refresh() {

        dataList.clear();
        for (int i = 0; i < 5; i++) {
            dataList.add(i);
        }
        adapter.notifyDataSetChanged();

        bd.refreshLayout.finishRefresh();
        bd.refreshLayout.setNoMoreData(false);
    }

    private void getData() {
       /* List<Friend> list = myTableManager.getAllFriend(curUser.getUserId());
        if (list != null) {
            dataList.clear();
            dataList.addAll(list);
            adapter.notifyDataSetChanged();
        }*/

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        myTableManager.closeDB();
    }
}
