package com.style.entity;

import android.util.Log;

public class KuaiDi {
    public String time;
    public String ftime;
    public String context;
    public String location;
    public int money;

    public KuaiDi() {
    }

    protected KuaiDi(String k) {

    }

    @Override
    public String toString() {
        return "KuaiDi{" +
                "time='" + time + '\'' +
                ", ftime='" + ftime + '\'' +
                ", context='" + context + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}
