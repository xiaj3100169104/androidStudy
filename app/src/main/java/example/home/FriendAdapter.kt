package example.home

import android.content.Context
import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.R
import com.style.framework.databinding.AdapterFriendBinding

import java.util.ArrayList

class FriendAdapter : BaseRecyclerViewAdapter<Int> {

    constructor(context: Context?, dataList: ArrayList<Int>) : super(context, dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bd : AdapterFriendBinding = getBinding(R.layout.adapter_friend, parent)
        return ViewHolder(bd)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val f = getData(position)
        holder.bd.viewMark.text = "数据$f"
        //holder.bd.viewNick.setText(f.getUser().getUserName());
        super.setOnItemClickListener(holder.itemView, position)
        holder.bd.executePendingBindings()
    }

    inner class ViewHolder : RecyclerView.ViewHolder {
        var bd: AdapterFriendBinding

        constructor(bd: AdapterFriendBinding) : super(bd.root) {
            this.bd = bd
        }
    }
}
