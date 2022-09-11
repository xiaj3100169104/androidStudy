package example.filedown

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.style.base.BaseRecyclerViewAdapter
import com.style.data.fileDown.FileDownloadStateBean
import com.style.data.fileDown.entity.CustomFileBean
import com.style.framework.R
import com.style.framework.databinding.FileDownListAdapterBinding
import java.util.*

class FileDownListAdapter : BaseRecyclerViewAdapter<CustomFileBean> {

    var mOptionListener: OnClickOptionListener<CustomFileBean>? = null

    constructor(context: Context?, dataList: ArrayList<CustomFileBean>) : super(context, dataList)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val bd: FileDownListAdapterBinding = FileDownListAdapterBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(bd)
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val holder = viewHolder as ViewHolder
        val f = list[position]
        holder.bd.progressBarDownSize.visibility = View.INVISIBLE
        holder.bd.viewName.text = f.title
        val state = f.fileStatus
        var btnDescribe = "下载"
        if (state != null) {
            when (state.status) {
                FileDownloadStateBean.DownStatus.NOT_DOWNLOAD -> btnDescribe = "下载"
                FileDownloadStateBean.DownStatus.DOWNLOADING -> {
                    btnDescribe = "暂停"
                    holder.bd.progressBarDownSize.visibility = View.VISIBLE
                    val progress = (state.downloadSize.toFloat() / state.totalSize.toFloat() * 100).toInt()
                    holder.bd.progressBarDownSize.setProgress(progress)
                }
                FileDownloadStateBean.DownStatus.DOWNLOAD_PAUSE -> {
                    btnDescribe = "继续"
                    holder.bd.progressBarDownSize.visibility = View.VISIBLE
                    val progress = (state.downloadSize.toFloat() / state.totalSize.toFloat() * 100).toInt()
                    holder.bd.progressBarDownSize.setProgress(progress)
                }
                FileDownloadStateBean.DownStatus.DOWNLOAD_COMPLETED -> btnDescribe = "打开"
            }
        }
        holder.bd.btnOption.text = btnDescribe
        holder.bd.btnOption.setOnClickListener {
            mOptionListener?.onClickOption(position, f)
        }
        super.setOnItemClickListener(holder.itemView, position)
        super.setOnItemLongClickListener(holder.itemView, position)
    }

    class ViewHolder : RecyclerView.ViewHolder {
        var bd: FileDownListAdapterBinding

        constructor(bd: FileDownListAdapterBinding) : super(bd.root) {
            this.bd = bd
        }
    }

    fun setOnClickOptionListener(listener: OnClickOptionListener<CustomFileBean>) {
        this.mOptionListener = listener
    }

    interface OnClickOptionListener<CustomFileBean> {
        fun onClickOption(position: Int, data: CustomFileBean)
    }
}
