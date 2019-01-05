package example.filedown

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import com.style.app.FileDirConfig
import com.style.base.BaseDefaultTitleBarActivity
import com.style.base.BaseRecyclerViewAdapter
import com.style.data.net.file.MultiThreadDownloadManager
import com.style.data.net.file.SingleFileDownloadTask
import com.style.framework.R
import com.style.threadPool.CustomFileDownloadManager
import com.style.view.systemHelper.DividerItemDecoration
import kotlinx.android.synthetic.main.file_down_list_activity.*
import java.util.ArrayList

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
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener(object : BaseRecyclerViewAdapter.OnItemClickListener<CustomFileBean> {
            override fun onItemClick(position: Int, data: CustomFileBean) {
                showToast(position.toString() + "")
                cancelDownloadTask(data)
            }
        })
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
        dataList.clear()
        dataList.addAll(list!!)
        adapter.notifyDataSetChanged()
        downloadAll()
    }

    private fun downloadAll() {
        dataList.forEachIndexed { index, f ->
            val task = SingleFileDownloadTask(f.url, FileDirConfig.DIR_APP_FILE.plus("/").plus(f.fileName))
            CustomFileDownloadManager.getInstance().addDownloadTask(f.url, task)
        }
    }

    private fun cancelDownloadTask(data: CustomFileBean) {
        CustomFileDownloadManager.getInstance().cancelDownloadTask(data.url)
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
        super.onDestroy()
        MultiThreadDownloadManager.getInstance().cancelCallback(TAG)
    }
}
