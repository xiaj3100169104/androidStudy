package example.home.contact

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseNoPagerLazyRefreshFragment
import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.R
import kotlinx.android.synthetic.main.fragment_home_2.*
import example.home.MainViewModel
import kotlin.collections.ArrayList


class HomeListFragment : BaseNoPagerLazyRefreshFragment() {
    private var pageNo: Int = 1
    private lateinit var mViewModel: ContactViewModel
    private lateinit var mHostViewModel: MainViewModel
    private lateinit var dataList: ArrayList<Int>
    private lateinit var adapter: FriendAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_home_2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHostViewModel = ViewModelProviders.of(this.activity!!).get(MainViewModel::class.java)
        mViewModel = ViewModelProviders.of(this).get(ContactViewModel::class.java)
        dataList = ArrayList()
        adapter = FriendAdapter(context, dataList)
        val layoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(com.style.view.diviver.DividerItemDecoration(context))
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
        refresh()
    }

    private fun loadMore() {
        pageNo++
        val t= getData()
        refreshLayout.complete()
        onDataResult(t)
    }

    private fun refresh() {
        pageNo = 1
        val t= getData()
        refreshLayout.complete()
        onDataResult(t)
    }

    private fun onDataResult(t: ArrayList<Int>) {
        if (pageNo == 1)
            dataList.clear()
        dataList.addAll(t)
        adapter.notifyDataSetChanged()
        if (t.isNullOrEmpty()) {
            if (pageNo == 1)
                refreshLayout.setEnableLoadMore(false)
            else
                refreshLayout.finishLoadMoreWithNoMoreData()//将不会再次触发加载更多事件并显示提示文字,不需显示使用setEnableLoadMore
        } else {//还可以加载更多
            refreshLayout.setEnableLoadMore(true)
            refreshLayout.setNoMoreData(false)
        }

    }

    private fun getData(): ArrayList<Int> {
        val list = arrayListOf<Int>()
        if (pageNo == 4)
            return list
        for (i in 0 until 5) {
            list.add(i)
        }
        return list
    }

}
