package com.style.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.style.bean.User;
import com.style.db.user.UserDBManager;

public class AccountManager {
    protected String TAG = getClass().getSimpleName();

    private static final String LOGIN_INFO = "loginInfo";
    private static final String CURRENT_ACCOUNT = "currentAccount";
    private static final String ACCOUNT_All = "accountAll";

    private static final String IS_AUTO_LOGIN = "is_auto_login";
    private static final String PASSWORD = "password";
    private static final String SIGN_KEY = "signKey";

    private Context context;
    private User currentUser;

    private static AccountManager mInstance;
    private static final Object mLock = new Object();

    //避免同时获取多个实例
    public synchronized static AccountManager getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new AccountManager();
            }
            return mInstance;
        }
    }

    public void init(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    //当前登录账号和登录过的所有账号
    protected SharedPreferences getLoginSharedPreferences() {
        SharedPreferences sp = getContext().getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
        return sp;
    }

    //登录账户的其他信息
    protected SharedPreferences getUserSharedPreferences(String account) {
        SharedPreferences sp = getContext().getSharedPreferences(String.valueOf(account), Context.MODE_PRIVATE);
        return sp;
    }

    public void setCurrentAccount(String account) {
        SharedPreferences.Editor editor = getLoginSharedPreferences().edit();
        editor.putString(CURRENT_ACCOUNT, account).apply();// 异步真正提交到硬件磁盘,
        // 而commit是同步的提交到硬件磁盘
        //addAccount(account);
    }

    public String getCurrentAccount() {
        String value = getLoginSharedPreferences().getString(CURRENT_ACCOUNT, null);
        return value;
    }


    public void setCurrentUser(User user) {
        if (user != null) {
            String account = user.getUserId();
            setCurrentAccount(user.getUserId());
            setPassword(account, user.getPassword());
            setSignKey(user.getSignKey());
            UserDBManager.getInstance().insertUser(user);
            currentUser = getCurrentUser();
        }
    }


    public User getCurrentUser() {
        if (currentUser == null) {
            String account = getCurrentAccount();
            if (!TextUtils.isEmpty(account))
                currentUser = getUser(account);
        }
        return currentUser;
    }

    public User getUser(String account) {
        User user = UserDBManager.getInstance().getUser(account);
        if (user != null) {
            user.setPassword(getPassword(account));
            user.setSignKey(getSignKey());
        }
        return user;
    }

    public void clearUser(String account) {
        currentUser = null;
        setPassword(account, "");
        setSignKey("");
    }

    public void setIsAutoLogin(String account, boolean value) {
        SharedPreferences.Editor editor = getUserSharedPreferences(account).edit();
        editor.putBoolean(IS_AUTO_LOGIN, value).apply();
    }

    public boolean getIsAutoLogin(String account) {
        boolean value = getUserSharedPreferences(account).getBoolean(IS_AUTO_LOGIN, true);
        return value;
    }

    public void setSignKey(String signKey) {
        SharedPreferences.Editor editor = getLoginSharedPreferences().edit();
        editor.putString(SIGN_KEY, signKey).apply();
    }

    public String getSignKey() {
        String value = "Bearer " + getLoginSharedPreferences().getString(SIGN_KEY, "");
        return value;

    }

    public void setPassword(String account, String password) {
        SharedPreferences.Editor editor = getUserSharedPreferences(account).edit();
        editor.putString(PASSWORD, password).apply();
    }

    public String getPassword(String account) {
        String value = getUserSharedPreferences(account).getString(PASSWORD, "");
        return value;
    }

    public void saveBleAddress(String address) {
        getLoginSharedPreferences().edit().putString("bleAddress", address).apply();
    }

    public String getBleAddress() {
        return getLoginSharedPreferences().getString("bleAddress", "");
    }
}
