package com.style.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "user_info_table", foreignKeys = @ForeignKey(entity = UserBean.class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE))
public class UserInfo implements Serializable {

    @PrimaryKey
    @ColumnInfo(name = "user_id")
    public String userId;
    @ColumnInfo(name = "tel_phone")
    public String telPhone;
    @ColumnInfo(name = "password")
    public String password;
    @ColumnInfo(name = "username")
    public String userName;
    @ColumnInfo(name = "sex")
    public String sex;
    @Ignore
    public String signKey;

    public UserInfo() {
    }

    public UserInfo(String sex) {
        this.sex = sex;
    }

    public UserInfo(String telPhone, String password) {
        this.telPhone = telPhone;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTelPhone() {
        return telPhone;
    }

    public void setTelPhone(String telPhone) {
        this.telPhone = telPhone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSignKey() {
        return signKey;
    }

    public void setSignKey(String signKey) {
        this.signKey = signKey;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "userId='" + userId + '\'' +
                ", telPhone='" + telPhone + '\'' +
                ", password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                ", sex='" + sex + '\'' +
                ", signKey='" + signKey + '\'' +
                '}';
    }
}
