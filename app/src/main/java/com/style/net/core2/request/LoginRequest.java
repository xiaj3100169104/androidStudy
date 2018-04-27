package com.style.net.core2.request;

/**
 * Created by xiajun on 2018/4/27.
 */

public class LoginRequest {
    public LoginRequest(String userName, String password) {
        UserName = userName;
        Password = password;
    }

    public String UserName;
    public String Password;
}
