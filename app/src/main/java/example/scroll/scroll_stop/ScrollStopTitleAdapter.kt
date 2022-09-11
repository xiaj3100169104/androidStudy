package example.scroll.scroll_stop

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.databinding.ScrollStopTitleAdapterBinding

import java.util.ArrayList

class ScrollStopTitleAdapter : BaseRecyclerViewAdapter<String> {

    constructor(context: Context?, dataList: ArrayList<String>) : super(context, dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bd: ScrollStopTitleAdapterBinding = ScrollStopTitleAdapterBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bd)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val f = list[position]
        val s = "分类$f"
        holder.bd.tvTitle.text = s
        //holder.bd.viewNick.setText(f.getUser().getUserName());
        super.setOnItemClickListener(holder.itemView, position)
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var bd: ScrollStopTitleAdapterBinding

        constructor(bd: ScrollStopTitleAdapterBinding) : super(bd.root) {
            this.bd = bd
        }
    }
}
