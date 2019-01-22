package com.style.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.style.framework.BuildConfig;

@Entity(tableName = "user_table")
public class UserBean {
    @PrimaryKey
    @ColumnInfo(name = "id")
    @NonNull
    public String id;
    @ColumnInfo(name = "name")
    public String name;
    @ColumnInfo(name = "phone")
    public String phone;
    @ColumnInfo(name = "sex")
    public int sex;
    @ColumnInfo(name = "updateTime")
    public long updateTime;
    @Ignore
    public int age;

    public UserBean() {
    }

    public UserBean(@NonNull String id, String name, String phone, int sex, long updateTime, int age) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.sex = sex;
        this.updateTime = updateTime;
        this.age = age;
    }

    @Override
    public String toString() {
        return "TestRoom{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", sex=" + sex +
                ", updateTime=" + updateTime +
                ", age=" + age +
                '}';
    }
}
