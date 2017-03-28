package com.style.net.param;


import com.style.manager.AccountManager;

public class TokenRequestParams {
    private String token;

    public TokenRequestParams(String uri) {
        this.token = AccountManager.getInstance().getToken();
    }
}
