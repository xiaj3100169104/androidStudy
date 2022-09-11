package example.home.contact

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup

import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.R
import com.style.framework.databinding.AdapterFriendBinding

import java.util.ArrayList

class FriendAdapter : BaseRecyclerViewAdapter<Int> {

    constructor(context: Context?, dataList: ArrayList<Int>) : super(context, dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bd: AdapterFriendBinding = AdapterFriendBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bd)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val f = list[position]
        val s = "数据$f"
        holder.bd.viewMark.text = s
        //holder.bd.viewNick.setText(f.getUser().getUserName());
        super.setOnItemClickListener(holder.itemView, position)
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var bd: AdapterFriendBinding

        constructor(bd: AdapterFriendBinding) : super(bd.root) {
            this.bd = bd
        }
    }
}
