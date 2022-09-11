package example.home.contact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.style.base.BaseNoPagerLazyRefreshFragment
import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.databinding.FragmentHome2Binding
import com.style.view.diviver.DividerItemDecoration
import example.home.MainViewModel


class HomeListFragment : BaseNoPagerLazyRefreshFragment() {

    private lateinit var bd: FragmentHome2Binding
    private var pageNo: Int = 1
    private lateinit var mViewModel: ContactViewModel
    private lateinit var mHostViewModel: MainViewModel
    private lateinit var dataList: ArrayList<Int>
    private lateinit var adapter: FriendAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bd = FragmentHome2Binding.inflate(inflater, container, false)
        return bd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mHostViewModel = ViewModelProvider(this.requireActivity()).get(MainViewModel::class.java)
        mViewModel = ViewModelProvider(this).get(ContactViewModel::class.java)
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
        refresh()
    }

    private fun loadMore() {
        pageNo++
        val t= getData()
        bd.refreshLayout.complete()
        onDataResult(t)
    }

    private fun refresh() {
        pageNo = 1
        val t= getData()
        bd.refreshLayout.complete()
        onDataResult(t)
    }

    private fun onDataResult(t: ArrayList<Int>) {
        if (pageNo == 1)
            dataList.clear()
        dataList.addAll(t)
        adapter.notifyDataSetChanged()
        if (t.isNullOrEmpty()) {
            if (pageNo == 1)
                bd.refreshLayout.setEnableLoadMore(false)
            else
                bd.refreshLayout.finishLoadMoreWithNoMoreData()//将不会再次触发加载更多事件并显示提示文字,不需显示使用setEnableLoadMore
        } else {//还可以加载更多
            bd.refreshLayout.setEnableLoadMore(true)
            bd.refreshLayout.setNoMoreData(false)
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
