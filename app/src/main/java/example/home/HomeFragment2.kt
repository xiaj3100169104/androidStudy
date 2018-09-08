package example.home

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.style.base.BaseFragment
import com.style.base.BaseRecyclerViewAdapter
import com.style.bean.User
import com.style.data.db.user.UserDBManager
import com.style.framework.R
import com.style.framework.databinding.FragmentHome2Binding
import com.style.data.prefs.AccountManager
import com.style.view.DividerItemDecoration

import java.util.ArrayList


class HomeFragment2 : BaseFragment() {
    private lateinit var bd: FragmentHome2Binding
    private lateinit var dataList: ArrayList<Int>
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var adapter: FriendAdapter

    private lateinit var myTableManager: UserDBManager

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_2
    }

    override fun initData() {
        bd = getBinding()
        myTableManager = UserDBManager.getInstance()

        dataList = ArrayList()
        adapter = FriendAdapter(context, dataList)
        layoutManager = LinearLayoutManager(context)
        bd.recyclerView.layoutManager = layoutManager
        bd.recyclerView.addItemDecoration(DividerItemDecoration(context))
        bd.recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener<Int> {
            override fun onItemClick(position: Int, data: Int) {
                showToast(position.toString() + "")
            }
        })
        bd.refreshLayout.isEnableAutoLoadMore = true//开启自动加载功能（非必须）
        bd.refreshLayout.setOnRefreshListener { refreshLayout -> refreshLayout.layout.postDelayed({ refresh() }, 2000) }
        bd.refreshLayout.setOnLoadMoreListener { refreshLayout ->
            refreshLayout.layout.postDelayed({
                if (adapter.itemCount > 20) {
                    Toast.makeText(context, "数据全部加载完毕", Toast.LENGTH_SHORT).show()
                    refreshLayout.finishLoadMoreWithNoMoreData()//将不会再次触发加载更多事件
                } else {
                    loadMore()
                }
            }, 2000)
        }

        //触发自动刷新
        bd.refreshLayout.autoRefresh()
        //getData();
    }

    private fun loadMore() {
        val k = dataList.size
        for (i in k until k + 5) {
            dataList.add(i)
        }
        adapter.notifyDataSetChanged()

        bd.refreshLayout.finishLoadMore()
    }

    private fun refresh() {

        dataList.clear()
        for (i in 0..4) {
            dataList.add(i)
        }
        adapter.notifyDataSetChanged()

        bd.refreshLayout.finishRefresh()
        bd.refreshLayout.setNoMoreData(false)
    }

    private fun getData() {
        /* List<Friend> list = myTableManager.getAllFriend(curUser.getUserId());
        if (list != null) {
            dataList.clear();
            dataList.addAll(list);
            adapter.notifyDataSetChanged();
        }*/

    }


    override fun onDestroy() {
        super.onDestroy()
        myTableManager.closeDB()
    }
}
