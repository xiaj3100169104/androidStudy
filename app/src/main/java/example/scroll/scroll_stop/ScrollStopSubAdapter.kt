package example.scroll.scroll_stop

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.databinding.ScrollStopContentSubAdapterBinding

import java.util.ArrayList

class ScrollStopSubAdapter : BaseRecyclerViewAdapter<String> {

    constructor(context: Context?, dataList: ArrayList<String>) : super(context, dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bd: ScrollStopContentSubAdapterBinding = ScrollStopContentSubAdapterBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bd)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val f = list[position]
        val s = "数据$f"
        holder.bd.tvName.text = s
        super.setOnItemClickListener(holder.itemView, position)
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var bd: ScrollStopContentSubAdapterBinding

        constructor(bd: ScrollStopContentSubAdapterBinding) : super(bd.root) {
            this.bd = bd
        }
    }
}
