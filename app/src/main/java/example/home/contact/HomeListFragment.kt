package example.home.contact

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseNoPagerLazyRefreshFragment
import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.R
import com.style.view.diviver.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_home_2.*
import java.util.*
import example.home.MainViewModel


class HomeListFragment : BaseNoPagerLazyRefreshFragment() {
    private lateinit var mViewModel: ContactViewModel
    private lateinit var mHostViewModel: MainViewModel
    private lateinit var dataList: ArrayList<Int>
    private lateinit var adapter: FriendAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHostViewModel = getHostViewModel(MainViewModel::class.java)
        mViewModel = getViewModel(ContactViewModel::class.java)
        dataList = ArrayList()
        adapter = FriendAdapter(context, dataList)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(context))
        recyclerView.adapter = adapter
        //bd.refreshLayout.setRefreshHeader( SimpleRefreshHeader(context))
        adapter.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener<Int> {
            override fun onItemClick(position: Int, data: Int) {
                showToast(position.toString() + "")
            }
        })
        refreshLayout.isEnableLoadMore = false
        refreshLayout.isEnableAutoLoadMore = true//开启自动加载功能（非必须）
        refreshLayout.setOnRefreshListener { refreshLayout ->
            refreshLayout.layout.postDelayed({
                refresh()
            }, 1000)
        }
        refreshLayout.setOnLoadMoreListener { refreshLayout ->
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
        refreshLayout.complete()
        if (dataList.size > 15)
            refreshLayout.finishLoadMoreWithNoMoreData()//将不会再次触发加载更多事件并显示提示文字,不需显示使用setEnableLoadMore
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
        refreshLayout.complete()
        if (dataList.size > 0) {
            refreshLayout.isEnableLoadMore = true
            refreshLayout.setNoMoreData(false)
        } else {
            refreshLayout.isEnableLoadMore = false
            //refreshLayout.setNoMoreData(true)
        }
        //refreshLayout.finishLoadMoreWithNoMoreData()
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
