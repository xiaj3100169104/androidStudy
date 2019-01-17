package com.style.entity;

import android.text.TextUtils;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/5/23.
 */

public class UserInfo implements Serializable {
    /**
     * Id : 0
     * NickName : string
     * Mobile : string
     * Gender : 1
     * Birthday : 2017-05-23T06:35:02.219Z
     * Height : 0.1
     * Weight : 0.1
     */

    private int Id;
    private String UserName;
    private String UserPwd;
    /**
     * Role : 0
     * Height : 0
     * Weight : 0
     * Avatar : string
     */

    private int Role;

    public String getAreaCode() {
        return AreaCode;
    }

    public void setAreaCode(String areaCode) {
        AreaCode = areaCode;
    }

    private String AreaCode;

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPwd() {
        return UserPwd;
    }

    public void setUserPwd(String userPwd) {
        UserPwd = userPwd;
    }

    private String NickName;
    private String Mobile;
    private int Gender;
    private String Birthday;
    private int Height;
    private int Weight;

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getNickName() {
        return NickName;
    }

    public void setNickName(String NickName) {
        this.NickName = NickName;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String Mobile) {
        this.Mobile = Mobile;
    }

    public int getGender() {
        return Gender;
    }

    public void setGender(int Gender) {
        this.Gender = Gender;
    }

    public String getBirthday() {
        if (!TextUtils.isEmpty(Birthday) && Birthday.contains("T")) {
            Birthday = Birthday.split("T")[0];
        }
        return Birthday;
    }

    public void setBirthday(String Birthday) {
        this.Birthday = Birthday;
    }

    public int getHeight() {
        return Height;
    }

    public void setHeight(int Height) {
        this.Height = Height;
    }

    public int getWeight() {
        return Weight;
    }

    public void setWeight(int Weight) {
        this.Weight = Weight;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "Id=" + Id +
                ", UserName='" + UserName + '\'' +
                ", UserPwd='" + UserPwd + '\'' +
                ", AreaCode='" + AreaCode + '\'' +
                ", NickName='" + NickName + '\'' +
                ", Mobile='" + Mobile + '\'' +
                ", Gender=" + Gender +
                ", Birthday='" + Birthday + '\'' +
                ", Height=" + Height +
                ", Weight=" + Weight +
                '}';
    }

    public int getRole() {
        return Role;
    }

    public void setRole(int Role) {
        this.Role = Role;
    }

}
