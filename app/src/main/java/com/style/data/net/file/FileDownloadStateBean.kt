package com.style.data.net.file

/**
 * 文件下载状态事件实体
 */
class FileDownloadStateBean {
    var url: String? = null
    var status = DownStatus.NOT_DOWNLOAD
    var totalSize = 0
    var downloadSize = 0

    constructor(url: String?, status: Int, totalSize: Int, downloadSize: Int) {
        this.url = url
        this.status = status
        this.totalSize = totalSize
        this.downloadSize = downloadSize
    }

    constructor(url: String?) {
        this.url = url
    }

    companion object {
        open class DownStatus {
            companion object {
                const val NOT_DOWNLOAD = 0
                const val DOWNLOAD_PREPARE = 1//排队中
                const val DOWNLOADING = 2
                const val DOWNLOAD_PAUSE = 3
                const val DOWNLOAD_COMPLETED = 4
            }
        }
    }

}