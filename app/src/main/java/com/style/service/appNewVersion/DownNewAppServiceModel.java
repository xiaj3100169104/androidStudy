package com.style.service.appNewVersion;


import android.annotation.SuppressLint;
import android.app.Application;
import androidx.lifecycle.MutableLiveData;
import androidx.annotation.NonNull;

import com.style.base.BaseServiceModel;

import java.io.File;

public class DownNewAppServiceModel extends BaseServiceModel {
    MutableLiveData<Boolean> downLoadNewVersionSuccess = new MutableLiveData<>();

    private File mAppFile;

    public DownNewAppServiceModel(@NonNull Application application) {
        super();
    }

    /**
     * 开始下载新版App
     * @param versionInfo
     */
    @SuppressLint("CheckResult")
    public void startDownloadApp(String versionInfo) {
        //执行子线程run中的代码逻辑
    }

}
