package example.filedown

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.style.base.BaseRecyclerViewAdapter
import com.style.framework.R
import com.style.framework.databinding.FileDownListAdapterBinding
import java.util.*

class FileDownListAdapter : BaseRecyclerViewAdapter<CustomFileBean> {

    private var mOptionListener: OnClickOptionListener<CustomFileBean>? = null

    constructor(context: Context?, dataList: ArrayList<CustomFileBean>) : super(context, dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bd: FileDownListAdapterBinding = getBinding(R.layout.file_down_list_adapter, parent)
        return ViewHolder(bd)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val f = getData(position)
        holder.bd.viewName.text = f.name
        var s = ""
        when (f.status) {
            CustomFileBean.Companion.DownStatus.NOT_DOWNLOAD -> s = "下载"
            CustomFileBean.Companion.DownStatus.DOWNLOADING -> s = "暂停"
            CustomFileBean.Companion.DownStatus.PAUSE_DOWNLOAD -> s = "继续"
            CustomFileBean.Companion.DownStatus.DOWNLOAD_COMPLETED -> s = "打开"
        }
        holder.bd.btnOption.text = s
        val progress = (f.downloadSize.toFloat() / f.totalSize.toFloat() * 100).toInt()
        holder.bd.progressBarDownSize.setProgress(progress)
        holder.bd.btnOption.setOnClickListener {
            mOptionListener?.onClickOption(position, f)
        }
        super.setOnItemClickListener(holder.itemView, position)
        holder.bd.executePendingBindings()
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var bd: FileDownListAdapterBinding

        constructor(bd: FileDownListAdapterBinding) : super(bd.root) {
            this.bd = bd
        }
    }

    interface OnClickOptionListener<CustomFileBean> {
        fun onClickOption(position: Int, data: CustomFileBean)
    }
}
