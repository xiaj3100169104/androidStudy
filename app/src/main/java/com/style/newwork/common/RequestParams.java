package com.style.newwork.common;

import android.text.TextUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiajun on 2017/1/4.
 */

public class RequestParams {
    public String url;
    private Map<String, String> params;
    private File[] files;

    public RequestParams(String url) {
        this.url = url;
        params = new HashMap<>();
    }

    public void addParameter(String key, String value) {
        params.put(key, value);
    }

    public void putPaths(String[] paths) {
        if (paths != null) {
            int length = paths.length;
            files = new File[length];
            for (int i = 0; i < length; i++) {
                if (!TextUtils.isEmpty(paths[i])) {
                    files[i] = new File(paths[i]);
                }
            }
        }
    }

    public File[] getFiles() {
        return files;
    }

    public void putFiles(File[] file) {
        files = file;
    }

    public Map<String, String> getParams() {
        return params;
    }

}
