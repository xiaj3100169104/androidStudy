package com.style.data.fileDown.multiBlock;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiajun on 2016/12/22.
 */
public class MultiThreadDownloadManager {
    protected String TAG = getClass().getSimpleName();

    private Map<Object, MultiThreadDownloadFileTask> taskMap = new HashMap<>();
    private static MultiThreadDownloadManager instance;

    public synchronized static MultiThreadDownloadManager getInstance() {
        if (instance == null) {
            instance = new MultiThreadDownloadManager();
        }
        return instance;
    }

    public void down(Object tag, String url, String targetPath, FileCallback callback) {
        MultiThreadDownloadFileTask task = taskMap.get(tag);
        if (task == null) {
            task = new MultiThreadDownloadFileTask(tag, url, 5, targetPath, callback);
            task.start();
            taskMap.put(tag, task);
        }
        task.setCallback(callback);
        task.setCanCallback(true);
    }

    public void cancelCallback(Object tag) {
        MultiThreadDownloadFileTask task = taskMap.get(tag);
        if (task != null)
            task.setCanCallback(false);
    }
    public void removeTask(Object tag) {
        taskMap.remove(tag);
    }
}

