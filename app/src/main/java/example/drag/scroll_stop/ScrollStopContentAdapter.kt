package example.drag.scroll_stop

import android.content.Context
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.R
import com.style.framework.databinding.ScrollStopContentAdapterBinding
import java.util.*

class ScrollStopContentAdapter : BaseRecyclerViewAdapter<String> {

    constructor(context: Context?, dataList: ArrayList<String>) : super(context, dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val bd: ScrollStopContentAdapterBinding = getBinding(R.layout.scroll_stop_content_adapter, parent)
        return ViewHolder(bd)
    }

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val f = getData(position)
        val s = "分类$f"
        holder.bd.tvTitle.text = s
        var dataList = ArrayList<String>()
        for (i in 0 until 5) {
            dataList.add("$i")
        }
        val adapter = ScrollStopSubAdapter(getContext(), dataList)
        val lm = androidx.recyclerview.widget.GridLayoutManager(getContext(), 3)
        holder.bd.recyclerViewSub.layoutManager = lm
        holder.bd.recyclerViewSub.adapter = adapter
        holder.bd.recyclerViewSub.isNestedScrollingEnabled = false
        //super.setOnItemClickListener(holder.itemView, position)
        holder.bd.executePendingBindings()
    }

    class ViewHolder : androidx.recyclerview.widget.RecyclerView.ViewHolder {
        var bd: ScrollStopContentAdapterBinding

        constructor(bd: ScrollStopContentAdapterBinding) : super(bd.root) {
            this.bd = bd
        }
    }
}
