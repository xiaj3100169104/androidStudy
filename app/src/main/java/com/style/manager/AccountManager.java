package com.style.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.style.bean.User;
import com.style.db.custom.UserDBManager;

public class AccountManager {
    protected String TAG = getClass().getSimpleName();

    private static final String IS_AUTO_LOGIN = "is_auto_login";
    private static final String PASSWORD = "password";
    private static final String SIGN_KEY = "signKey";

    private Context context;
    private User currentUser;

    private static AccountManager mInstance;
    //避免同时获取多个实例
    public synchronized static AccountManager getInstance() {
        if (mInstance == null) {
            mInstance = new AccountManager();
        }
        return mInstance;
    }

    public void init(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setCurrentUser(User user) {
        currentUser = user;
        /*if (user != null) {
            String account = user.getAccount();
            clearUser(account);
            AppManager.getInstance().putCurrentAccount(user.getAccount());
            putPassword(account, user.getPassword());
            putSignKey(account, user.getSignKey());
            currentUser = getUser(account);
        }*/
    }


    public User getCurrentUser() {
        if (currentUser == null) {
            User user = new User(8, "18202823096", "123456", "夏军", null);
            UserDBManager.getInstance().insertOrUpdateUser(user);
            String account = AppManager.getInstance().getCurrentAccount();
            currentUser = getUser(account);
        }
        return currentUser;
    }

    public User getUser(String account) {
        User user = null;
        if (!TextUtils.isEmpty(account)) {
            user = new User();
            user.setAccount(account);
            user.setPassword(getPassword(account));
            user.setSignKey(getSignKey(account));
        }
        return user;
    }

    public void clearUser(String account) {
        putPassword(account, "");
        putSignKey(account, "");
    }

    protected SharedPreferences getUserInfo(String account) {
        SharedPreferences sp = getContext().getSharedPreferences(account, Context.MODE_PRIVATE);
        return sp;
    }

    public void putIsAutoLogin(String account, boolean value) {
        SharedPreferences.Editor editor = getUserInfo(account).edit();
        editor.putBoolean(IS_AUTO_LOGIN, value).apply();
    }

    public boolean getIsAutoLogin(String account) {
        boolean value = getUserInfo(account).getBoolean(IS_AUTO_LOGIN, true);
        return value;
    }

    public void putSignKey(String account, String signKey) {
        SharedPreferences.Editor editor = getUserInfo(account).edit();
        editor.putString(SIGN_KEY, signKey).apply();
    }

    public String getSignKey(String account) {
        String value = getUserInfo(account).getString(SIGN_KEY, "");
        return value;

    }

    public void putPassword(String account, String password) {
        if (!TextUtils.isEmpty(account)) {
            SharedPreferences.Editor editor = getUserInfo(account).edit();
            editor.putString(PASSWORD, password).apply();
        }
    }

    public String getPassword(String account) {
        String value = getUserInfo(account).getString(PASSWORD, "");
        return value;
    }

    public String getToken() {
        return null;
    }
}
