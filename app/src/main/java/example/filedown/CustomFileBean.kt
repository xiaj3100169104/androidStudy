package example.filedown

/**
 * 根据sd卡文件数据下载了多少更新界面下载进度
 */
class CustomFileBean {
    var url: String? = null
    var title: String? = null
    var fileName: String? = null
    var totalSize = 0
    var downloadSize = 0
    var status = DownStatus.NOT_DOWNLOAD

    constructor(url: String?, title: String?, fileName: String?) {
        this.url = url
        this.title = title
        this.fileName = fileName
    }

    companion object {
        class DownStatus {
            companion object {
                const val NOT_DOWNLOAD = 0
                const val DOWNLOADING = 1
                const val PAUSE_DOWNLOAD = 2
                const val DOWNLOAD_COMPLETED = 3
            }
        }
    }

}