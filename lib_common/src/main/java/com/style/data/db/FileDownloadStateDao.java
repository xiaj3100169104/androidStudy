package com.style.data.db;

import androidx.room.*;
import com.style.data.fileDown.FileDownloadStateBean;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface FileDownloadStateDao {
    @Query("SELECT * FROM file_download")
    public List<FileDownloadStateBean> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long save(FileDownloadStateBean bean);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public int update(FileDownloadStateBean entity);

    @Query("UPDATE file_download SET status=:status,downloadSize=:downloadSize WHERE url=:url")
    public int update(int status, int downloadSize, String url);
}