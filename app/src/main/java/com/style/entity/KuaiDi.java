package com.style.entity;

public class KuaiDi {
    public String time;
    public String ftime;
    public String context;
    public String location;

    public KuaiDi() {
    }

    protected KuaiDi(String k){

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
