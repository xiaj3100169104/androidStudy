package example.drag.scroll_stop

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.R
import com.style.framework.databinding.ScrollStopTitleAdapterBinding

import java.util.ArrayList

class ScrollStopTitleAdapter : BaseRecyclerViewAdapter<String> {

    constructor(context: Context?, dataList: ArrayList<String>) : super(context, dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val bd: ScrollStopTitleAdapterBinding = getBinding(R.layout.scroll_stop_title_adapter, parent)
        return ViewHolder(bd)
    }

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val f = getData(position)
        val s = "分类$f"
        holder.bd.tvTitle.text = s
        //holder.bd.viewNick.setText(f.getUser().getUserName());
        super.setOnItemClickListener(holder.itemView, position)
        holder.bd.executePendingBindings()
    }

    class ViewHolder : androidx.recyclerview.widget.RecyclerView.ViewHolder {
        var bd: ScrollStopTitleAdapterBinding

        constructor(bd: ScrollStopTitleAdapterBinding) : super(bd.root) {
            this.bd = bd
        }
    }
}
