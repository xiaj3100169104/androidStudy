package example.filedown

import com.style.data.net.file.FileDownloadStateBean

/**
 * 根据sd卡文件数据下载了多少更新界面下载进度
 */
class CustomFileBean {
    var url: String? = null
    var title: String? = null
    var fileName: String? = null
    var totalSize = 0
    var fileStatus: FileDownloadStateBean? = null

    constructor(url: String?, title: String?, fileName: String?) {
        this.url = url
        this.title = title
        this.fileName = fileName
    }
}