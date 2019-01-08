package example.filedown

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.style.app.FileDirConfig
import com.style.base.BaseDefaultTitleBarActivity
import com.style.base.BaseRecyclerViewAdapter
import com.style.data.event.EventBusEvent
import com.style.data.net.file.FileDownloadStateBean
import com.style.data.net.file.FileDownloadStateBean.Companion.DownStatus
import com.style.data.net.file.MultiThreadDownloadManager
import com.style.framework.R
import com.style.utils.OpenFileUtil
import com.style.view.systemHelper.DividerItemDecoration
import kotlinx.android.synthetic.main.file_down_list_activity.*
import org.simple.eventbus.EventBus
import org.simple.eventbus.Subscriber
import org.simple.eventbus.ThreadMode
import java.io.File
import java.util.ArrayList
import android.support.v7.widget.SimpleItemAnimator
import com.style.data.net.exception.CustomRuntimeException


class FileDownActivity : BaseDefaultTitleBarActivity() {

    private val targetPath = FileDirConfig.DIR_APP_FILE + "/apache-tomcat-8.0.24_multi_thread.exe"

    private lateinit var dataList: ArrayList<CustomFileBean>
    private lateinit var adapter: FileDownListAdapter
    private lateinit var mViewModel: FileDownListViewModel

    override fun onCreate(arg0: Bundle?) {
        super.onCreate(arg0)
        setContentView(R.layout.file_down_list_activity)
        setToolbarTitle("文件下载")
        dataList = ArrayList()
        adapter = FileDownListAdapter(getContext(), dataList)
        val layoutManager = LinearLayoutManager(getContext())
        recyclerView.layoutManager = layoutManager
        recyclerView.addItemDecoration(DividerItemDecoration(getContext()))
        //解决默认动画造成的itemView闪烁
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener<CustomFileBean> {
            override fun onItemClick(position: Int, data: CustomFileBean) {
                logE("onItemClick", position.toString())
            }
        })
        adapter.setOnItemLongClickListener(object : BaseRecyclerViewAdapter.OnItemLongClickListener<CustomFileBean> {
            override fun onItemLongClick(itemView: View, position: Int, data: CustomFileBean) {
                logE("onItemLongClick", position.toString())
            }
        })
        adapter.setOnClickOptionListener(object : FileDownListAdapter.OnClickOptionListener<CustomFileBean> {
            override fun onClickOption(position: Int, data: CustomFileBean) {
                logE("onClickOption", position.toString())
                if (data.fileStatus == null) {
                    mViewModel.newDownloadFile(data)
                    return
                }
                val state = data.fileStatus
                when (state?.status) {
                    DownStatus.NOT_DOWNLOAD -> mViewModel.newDownloadFile(data)
                    DownStatus.DOWNLOAD_PREPARE -> {
                    }
                    DownStatus.DOWNLOAD_PAUSE -> mViewModel.continueDownload(data)
                    DownStatus.DOWNLOADING -> mViewModel.pauseDownloadTask(data)
                    DownStatus.DOWNLOAD_COMPLETED -> {
                        val file = File(FileDirConfig.DIR_APP_FILE, data.fileName)
                        if (file.parentFile.exists() && file.exists()) {
                            try {
                                OpenFileUtil.openFile(getContext(), file)
                            } catch (e: CustomRuntimeException) {
                                showToast(e.message!!)
                            }
                        } else {
                            //未下载
                            showToast("文件不存在或已被删除")
                            state.status = DownStatus.NOT_DOWNLOAD
                            adapter.notifyItemChanged(position)
                        }
                    }
                }
            }
        })
        EventBus.getDefault().register(this)
        mViewModel = getViewModel(FileDownListViewModel::class.java)
        mViewModel.files.observe(this, Observer<ArrayList<CustomFileBean>> { t ->
            refreshData(t)
        })
        getData()
    }

    private fun getData() {
        mViewModel.getData()
    }

    private fun refreshData(list: ArrayList<CustomFileBean>?) {
        if (list != null) {
            dataList.clear()
            list.forEachIndexed { index, b ->
                val file = File(FileDirConfig.DIR_APP_FILE, b.fileName)
                if (file.parentFile.exists() && file.exists()) {
                    if (b.fileStatus == null)
                        b.fileStatus = FileDownloadStateBean(b.url)
                    b.fileStatus?.status = DownStatus.DOWNLOAD_COMPLETED
                }
            }
            dataList.addAll(list)
            adapter.notifyDataSetChanged()
        }

    }

    @Subscriber(tag = EventBusEvent.FILE_DOWNLOAD_STATE_CHANGED, mode = ThreadMode.MAIN)
    private fun onFileDownloadStateChanged(data: FileDownloadStateBean) {
        for (i in dataList.indices) {
            if (data.url.equals(dataList[i].url)) {
                dataList[i].fileStatus = data
                adapter.notifyItemChanged(i)
                break
            }
        }
    }

    private fun multiThreadDownload() {
        /* MultiThreadDownloadManager.getInstance().down(TAG, url, targetPath, object : FileCallback() {
             override fun start(fileSize: Int) {
                 logE(TAG, "下载开始，文件大小==$fileSize")
             }

             override fun inProgress(currentDownSize: Int, fileSize: Int, progress: Float) {
                 bd.progressBar4.setProgress((100 * progress).toInt())
             }

             override fun complete(filePath: String) {
                 logE(TAG, "下载完成，文件路径==$filePath")
             }
         })*/
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
        MultiThreadDownloadManager.getInstance().cancelCallback(TAG)
    }
}
