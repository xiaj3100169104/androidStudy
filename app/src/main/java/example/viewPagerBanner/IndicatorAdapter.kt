package example.viewPagerBanner

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.R
import com.style.framework.databinding.AdapterIndicatorBinding

import kotlin.collections.ArrayList

class IndicatorAdapter : BaseRecyclerViewAdapter<Boolean> {

    constructor(context: Context?, dataList: ArrayList<Boolean>) : super(context, dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {
        val bd: AdapterIndicatorBinding = getBinding(R.layout.adapter_indicator, parent)
        return ViewHolder(bd)
    }

    override fun onBindViewHolder(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val f = getData(position)
        holder.bd.viewIndicator.isSelected = f
        super.setOnItemClickListener(holder.itemView, position)
        holder.bd.executePendingBindings()
    }

    fun setSelected(i: Int) {
        for (k in list.indices)
            list[k] = k == i
        notifyDataSetChanged()
    }

    class ViewHolder : androidx.recyclerview.widget.RecyclerView.ViewHolder {
        var bd: AdapterIndicatorBinding

        constructor(bd: AdapterIndicatorBinding) : super(bd.root) {
            this.bd = bd
        }
    }
}
