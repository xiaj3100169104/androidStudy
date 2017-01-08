package com.style.rxAndroid;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Subscription;

/**
 * 异步任务处理类
 * Created by xiajun on 2016/10/9.
 */

public class RXAsynTaskManager {
    private Map<String, List<Subscription>> mTaskMap = new HashMap();
    private static RXAsynTaskManager mInstance;

    public static RXAsynTaskManager getInstance() {
        if (mInstance == null) {
            mInstance = new RXAsynTaskManager();
        }
        return mInstance;
    }

    public Subscription runTask(String tag, BaseRXTaskCallBack callBack) {
        Subscription subscription = callBack.run();
        addSubscription(tag, subscription);
        return subscription;
    }

    protected void addSubscription(String tag, Subscription subscription) {
        List<Subscription> mSubscriptions = mTaskMap.get(tag);
        if (mSubscriptions == null)
            mSubscriptions = new ArrayList<>();
        mSubscriptions.add(subscription);
        mTaskMap.put(tag, mSubscriptions);
    }

    public void unsubscribeAll(String tag) {
        List<Subscription> mSubscriptions = mTaskMap.get(tag);
        if (mSubscriptions != null) {
            for (Subscription s : mSubscriptions) {
                if (s != null && s.isUnsubscribed()) {
                    s.unsubscribe();
                }
            }
        }
    }
}
