package com.style.data.prefs;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.style.entity.UserInfo;
import com.style.utils.AESCipher;

public class AppPrefsManager {

    private final String TAG = getClass().getSimpleName();

    private static final String IS_FIRST_LOGIN = "isFirstLogin";
    private static final String LOGIN_INFO = "loginInfo";
    private static final String CURRENT_ACCOUNT = "currentAccount";
    private static final String ACCOUNT_All = "accountAll";
    private static final String IS_AUTO_LOGIN = "is_auto_login";
    private static final String PASSWORD = "password";
    private static final String SIGN_KEY = "signKey";

    private static Object mLock = new Object();
    private static AppPrefsManager mInstance;

    public static  AppPrefsManager getInstance() {
        synchronized (mLock) {
            if (mInstance == null) {
                mInstance = new AppPrefsManager();
            }
            return mInstance;
        }
    }

    /* 私有构造方法，防止被JAVA默认的构造函数实例化 */
    private AppPrefsManager() {
    }

    private UserInfo currentUser;
    private SharedPreferences loginSharedPreferences;

    public void init(Context context) {
        loginSharedPreferences = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE);
    }

    public boolean isFirstOpen() {
        return loginSharedPreferences.getBoolean(IS_FIRST_LOGIN, true);
    }

    // 异步真正提交到硬件磁盘,
    // 而commit是同步的提交到硬件磁盘
    //addAccount(account);
    private String currentAccount;

    public String getCurrentAccount() {
        return loginSharedPreferences.getString(CURRENT_ACCOUNT, null);
    }

    public void setCurrentAccount(String account) {
        SharedPreferences.Editor editor = loginSharedPreferences.edit();
        editor.putString(CURRENT_ACCOUNT, account).apply();
    }

    private boolean isAutoLogin;

    public boolean isAutoLogin() {
        return loginSharedPreferences.getBoolean(IS_AUTO_LOGIN, true);
    }

    public void setAutoLogin(boolean autoLogin) {
        SharedPreferences.Editor editor = loginSharedPreferences.edit();
        editor.putBoolean(IS_AUTO_LOGIN, autoLogin).apply();
    }

    private String signKey;

    public String getSignKey() {
        return "Bearer " + loginSharedPreferences.getString(SIGN_KEY, "");
    }

    public void setSignKey(String signKey) {
        SharedPreferences.Editor editor = loginSharedPreferences.edit();
        editor.putString(SIGN_KEY, signKey).apply();
    }

    private String bleAddress;

    public String getBleAddress() {
        return loginSharedPreferences.getString("bleAddress", "");
    }

    public void putFirstOpen(boolean isFirstLogin) {
        SharedPreferences.Editor editor = loginSharedPreferences.edit();
        editor.putBoolean(IS_FIRST_LOGIN, isFirstLogin).apply();
    }

    public void setCurrentUser(UserInfo user) {
        if (user != null) {
            String account = user.userId;
            currentAccount = user.userId;
            setPassword(account, user.password);
            signKey = user.signKey;
            currentUser = getCurrentUser();
        }
    }

    public UserInfo getCurrentUser() {
        if (currentUser == null) {
            String account = currentAccount;
            if (!TextUtils.isEmpty(account))
                currentUser = getUser(account);
        }
        return currentUser;
    }

    private UserInfo getUser(String account) {
        UserInfo user = new UserInfo();
        if (user != null) {
            user.password = getPassword(account);
            user.signKey = signKey;
        }
        return user;
    }

    public void clearUser(String account) {
        currentUser = null;
        setPassword(account, "");
        signKey = "";
    }

    public void saveUserEncrypt(UserInfo user) {
        String beforeEncrypt = JSON.toJSONString(user);
        Log.e(TAG, "加密前 -> $beforeEncrypt");
        try {
            String k = AESCipher.aesEncryptString(beforeEncrypt, "abcdabcdabcdabcd");
            Log.e(TAG, "加密后 -> $k");
            loginSharedPreferences.edit().putString("userEncrypt", k).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getUserDecrypt() {
        String beforeDescrypt = loginSharedPreferences.getString("userEncrypt", "");
        Log.e(TAG, "解密前 -> " + beforeDescrypt);
        try {
            String k = AESCipher.aesDecryptString(beforeDescrypt, "abcdabcdabcdabcd");
            Log.e(TAG, "解密后 -> $k");
            UserInfo user = JSON.parseObject(k, UserInfo.class);
            user.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPassword(String account, String password) {
        SharedPreferences.Editor editor = loginSharedPreferences.edit();
        editor.putString(PASSWORD, password).apply();
    }

    public String getPassword(String account) {
        return loginSharedPreferences.getString(PASSWORD, "");
    }

    public void saveBleAddress(String address) {
        loginSharedPreferences.edit().putString("bleAddress", address).apply();
    }

}
