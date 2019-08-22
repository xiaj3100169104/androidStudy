package com.style.data.db

import android.arch.persistence.room.*
import com.style.data.fileDown.FileDownloadStateBean

@Dao
interface FileDownloadStateDao {
    @Query("SELECT * FROM file_download")
    fun getAll(): List<FileDownloadStateBean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(bean: FileDownloadStateBean): Long

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(entity: FileDownloadStateBean): Int

    @Query("UPDATE file_download SET status=:status,downloadSize=:downloadSize WHERE url=:url")
    fun update(status: Int, downloadSize: Int, url: String): Int
}