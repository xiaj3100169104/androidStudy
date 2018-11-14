package example.home

import android.support.v7.widget.LinearLayoutManager
import com.style.base.BaseFragment
import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.R
import com.style.framework.databinding.FragmentHome2Binding
import com.style.view.systemHelper.DividerItemDecoration
import java.util.*


class HomeListFragment : BaseFragment() {
    private lateinit var bd: FragmentHome2Binding
    private lateinit var dataList: ArrayList<Int>
    private lateinit var adapter: FriendAdapter

    override fun getLayoutResId(): Int {
        return R.layout.fragment_home_2
    }

    override fun initData() {
        bd = getBinding()
        dataList = ArrayList()
        adapter = FriendAdapter(context, dataList)
        val layoutManager = LinearLayoutManager(context)
        bd.recyclerView.layoutManager = layoutManager
        bd.recyclerView.addItemDecoration(DividerItemDecoration(context))
        bd.recyclerView.adapter = adapter
        //bd.refreshLayout.setRefreshHeader( SimpleRefreshHeader(context))
        adapter.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener<Int> {
            override fun onItemClick(position: Int, data: Int) {
                showToast(position.toString() + "")
            }
        })
        bd.refreshLayout.isEnableLoadMore = false
        bd.refreshLayout.isEnableAutoLoadMore = true//开启自动加载功能（非必须）
        bd.refreshLayout.setOnRefreshListener { refreshLayout ->
            refreshLayout.layout.postDelayed({
                refresh()
            }, 1000)
        }
        bd.refreshLayout.setOnLoadMoreListener { refreshLayout ->
            refreshLayout.layout.postDelayed({
                loadMore()
            }, 1000)
        }
        //触发自动刷新
        //bd.refreshLayout.autoRefresh()
        //getData();
    }

    private fun loadMore() {
        val k = dataList.size
        for (i in k until k + 5) {
            dataList.add(i)
        }
        adapter.notifyDataSetChanged()
        bd.refreshLayout.complete()
        if (dataList.size > 20)
            bd.refreshLayout.finishLoadMoreWithNoMoreData()//将不会再次触发加载更多事件并显示提示文字,不需显示使用setEnableLoadMore
        //列表为空的时候显示文字不太友好，使用一下方法
       /* if (dataList.isEmpty()) {
            bd.refreshLayout.isEnableLoadMore = false
        } else {
            bd.refreshLayout.isEnableLoadMore = true
        }*/
    }

    private fun refresh() {
        dataList.clear()
        for (i in 0..4) {
            dataList.add(i)
        }
        adapter.notifyDataSetChanged()
        bd.refreshLayout.complete()
        if (dataList.size > 0)
            bd.refreshLayout.setNoMoreData(false)
        else
            bd.refreshLayout.setNoMoreData(true)
    }

    private fun getData() {
        /* List<Friend> list = myTableManager.getAllFriend(curUser.getUserId());
        if (list != null) {
            dataList.clear();
            dataList.addAll(list);
            adapter.notifyDataSetChanged();
        }*/

    }

}
