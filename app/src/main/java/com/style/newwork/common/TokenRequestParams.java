package com.style.newwork.common;


import com.style.manager.AccountManager;

public class TokenRequestParams extends RequestParams {
    private String token;

    public TokenRequestParams(String uri) {
        super(uri);
        //this.token = AccountManager.getInstance().getToken();
    }
}
