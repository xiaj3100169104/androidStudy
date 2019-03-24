package com.style.data.fileDown

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.Ignore
import android.arch.persistence.room.PrimaryKey
import android.support.annotation.NonNull
import java.io.Serializable

/**
 * 文件下载状态事件实体
 */
@Entity(tableName = "file_download")
open class FileDownloadStateBean : Serializable {
    @PrimaryKey
    @ColumnInfo(name = "url")
    @NonNull
    var url: String = ""
    @ColumnInfo(name = "status")
    var status = DownStatus.NOT_DOWNLOAD
    @ColumnInfo(name = "totalSize")
    var totalSize = 0
    @ColumnInfo(name = "downloadSize")
    var downloadSize = 0

    constructor()

    @Ignore
    constructor(url: String?) {
        this.url = url!!
    }

    @Ignore
    constructor(url: String?, status: Int, totalSize: Int, downloadSize: Int) {
        this.url = url!!
        this.status = status
        this.totalSize = totalSize
        this.downloadSize = downloadSize
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

    override fun toString(): String {
        return "FileDownloadStateBean(url=$url, status=$status, totalSize=$totalSize, downloadSize=$downloadSize)"
    }

}