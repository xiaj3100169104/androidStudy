package com.style.entity;

import android.util.Log;

public class KuaiDi {
    public String time;
    public String ftime;
    public String context;
    public String location;
    public int money;
    private final Object mLock = new Object();

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

    public void saveMoney(int m) {
        synchronized (mLock) {
            money += m;
            try {
                Log.e("saveMoney", "正在存钱，请稍后。。。");
                Thread.sleep(3000);
                Log.e("saveMoney", "余额:" + money);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void takeMoney(int m) {
        synchronized (mLock) {
            money -= m;
            try {
                Log.e("takeMoney", "正在取钱，请稍后。。。");
                Thread.sleep(3000);
                Log.e("takeMoney", "余额:" + money);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void seeMoney() {
        synchronized (KuaiDi.class) {
            try {
                Log.e("seeMoney", "正在查询，请稍后。。。");
                Thread.sleep(3000);
                Log.e("seeMoney", "余额:" + money);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
