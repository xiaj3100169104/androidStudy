package example.drag.scroll_stop

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.R
import com.style.framework.databinding.ScrollStopContentSubAdapterBinding

import java.util.ArrayList

class ScrollStopSubAdapter : BaseRecyclerViewAdapter<String> {

    constructor(context: Context?, dataList: ArrayList<String>) : super(context, dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val bd: ScrollStopContentSubAdapterBinding = getBinding(R.layout.scroll_stop_content_sub_adapter, parent)
        return ViewHolder(bd)
    }

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val f = getData(position)
        val s = "数据$f"
        holder.bd.tvName.text = s
        super.setOnItemClickListener(holder.itemView, position)
        holder.bd.executePendingBindings()
    }

    class ViewHolder : androidx.recyclerview.widget.RecyclerView.ViewHolder {
        var bd: ScrollStopContentSubAdapterBinding

        constructor(bd: ScrollStopContentSubAdapterBinding) : super(bd.root) {
            this.bd = bd
        }
    }
}
