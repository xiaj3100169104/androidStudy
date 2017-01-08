package com.style.newwork.response;


import com.style.bean.Friend;

import java.io.Serializable;

public class LoginBean implements Serializable {
    public String token;
    public Friend friend;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }
}
