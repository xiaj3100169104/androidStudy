package com.style.data.fileDown;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;
import java.io.Serializable;

/**
 * 文件下载状态事件实体
 */
@Entity(tableName = "file_download")
public class FileDownloadStateBean implements Serializable {
    @PrimaryKey
    @ColumnInfo(name = "url")
    @NonNull
    public String url = "";
    @ColumnInfo(name = "status")
    public int status = DownStatus.NOT_DOWNLOAD;
    @ColumnInfo(name = "totalSize")
    public int totalSize = 0;
    @ColumnInfo(name = "downloadSize")
    public int downloadSize = 0;

    public FileDownloadStateBean() {
    }

    @Ignore
    public FileDownloadStateBean(@NonNull String url) {
        this.url = url;
    }

    @Ignore
    public FileDownloadStateBean(@NonNull String url, int status, int totalSize, int downloadSize) {
        this.url = url;
        this.status = status;
        this.totalSize = totalSize;
        this.downloadSize = downloadSize;
    }

    @NonNull
    public String getUrl() {
        return url;
    }

    public void setUrl(@NonNull String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getDownloadSize() {
        return downloadSize;
    }

    public void setDownloadSize(int downloadSize) {
        this.downloadSize = downloadSize;
    }

    public static final class DownStatus {
        public static final int NOT_DOWNLOAD = 0;
        public static final int DOWNLOAD_PREPARE = 1;//排队中
        public static final int DOWNLOADING = 2;
        public static final int DOWNLOAD_PAUSE = 3;
        public static final int DOWNLOAD_COMPLETED = 4;
    }

    @Override
    public String toString() {
        return "FileDownloadStateBean{" +
                "url=" + url +
                ", status=" + status +
                ", totalSize=" + totalSize +
                ", downloadSize=" + downloadSize +
                '}';
    }

}