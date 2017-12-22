package com.style.net.core2;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import io.reactivex.internal.disposables.ListCompositeDisposable;

/**
 * Created by xiajun on 2017/12/21.
 */

public class RetrofitManager {
    private static RetrofitManager manager;
    private Map<String, ListCompositeDisposable> mTaskMap = new HashMap();

    public static RetrofitManager getInstance() {
        if (manager == null) {
            synchronized (RetrofitManager.class) {
                if (manager == null)
                    manager = new RetrofitManager();
            }

        }
        return manager;
    }

    public void addTask(String tag, Disposable call) {
        ListCompositeDisposable callList = mTaskMap.get(tag);
        if (callList == null) {
            callList = new ListCompositeDisposable();
            mTaskMap.put(tag, callList);
        }
        callList.add(call);
    }

    public void removeTask(String tag) {
        ListCompositeDisposable callList = mTaskMap.get(tag);
        if (callList != null) {
            callList.clear();
        }
    }
}
