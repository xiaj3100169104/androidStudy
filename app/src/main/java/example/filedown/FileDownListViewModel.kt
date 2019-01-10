package example.filedown

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import com.style.app.FileDirConfig
import com.style.base.BaseViewModel
import com.style.data.net.file.SingleFileDownloadTask
import com.style.threadPool.CustomFileDownloadManager

class FileDownListViewModel(application: Application) : BaseViewModel(application) {
    private val urls = arrayListOf("http://archive.apache.org/dist/tomcat/tomcat-8/v8.0.24/bin/apache-tomcat-8.0.24.exe"
            , "http://sw.bos.baidu.com/sw-search-sp/software/13d93a08a2990/ChromeStandalone_55.0.2883.87_Setup.exe"
            , "http://wdl1.cache.wps.cn/wps/download/W.P.S.50.391.exe")

    private val titles = arrayListOf("tomcat"
            , "谷歌浏览器"
            , "wps")
    private val fileNames = arrayListOf("apache-tomcat-8.0.24.exe"
            , "ChromeStandalone_55.0.2883.87_Setup.exe"
            , "W.P.S.50.391.exe")

    val files = MutableLiveData<ArrayList<CustomFileBean>>()

    fun getData() {
        val da = arrayListOf<CustomFileBean>()
        urls.forEachIndexed { index, s ->
            val f = CustomFileBean(s, titles[index], fileNames[index])
            da.add(f)
        }
        files.postValue(da)
    }

    fun downloadAllFile(dataList: ArrayList<CustomFileBean>) {
        dataList.forEachIndexed { index, f ->
            val task = SingleFileDownloadTask(f.url, 0, FileDirConfig.DIR_APP_FILE.plus("/").plus(f.fileName))
            CustomFileDownloadManager.getInstance().addDownloadTask(f.url, task)
        }
    }

    fun pauseDownloadFile(data: CustomFileBean) {
        CustomFileDownloadManager.getInstance().stopDownloadTask(data.url)
    }

    //新下载任务
    fun newDownloadFile(f: CustomFileBean) {
        val task = SingleFileDownloadTask(f.url, 0, FileDirConfig.DIR_APP_FILE.plus("/").plus(f.fileName))
        CustomFileDownloadManager.getInstance().addDownloadTask(f.url, task)
    }

    //暂停后继续下载
    fun continueDownloadFile(f: CustomFileBean) {
        val task = SingleFileDownloadTask(f.url, f.fileStatus?.downloadSize!!, FileDirConfig.DIR_APP_FILE.plus("/").plus(f.fileName))
        CustomFileDownloadManager.getInstance().addDownloadTask(f.url, task)
    }
}