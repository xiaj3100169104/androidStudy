package com.style.data.prefs

import android.content.Context
import android.content.SharedPreferences
import android.text.TextUtils
import android.util.Log

import com.alibaba.fastjson.JSON
import com.style.bean.User
import com.style.data.db.user.UserDBManager
import com.style.utils.AESCipher

class AccountManager/* 私有构造方法，防止被JAVA默认的构造函数实例化 */ {

    val TAG = javaClass.simpleName

    companion object {
        private val IS_FIRST_LOGIN = "isFirstLogin"
        private val LOGIN_INFO = "loginInfo"
        private val CURRENT_ACCOUNT = "currentAccount"
        private val ACCOUNT_All = "accountAll"
        private val IS_AUTO_LOGIN = "is_auto_login"
        private val PASSWORD = "password"
        private val SIGN_KEY = "signKey"

        private val mLock = Any()
        private var mInstance: AccountManager? = null

        fun getInstance(): AccountManager {
            synchronized(mLock) {
                if (mInstance == null) {
                    mInstance = AccountManager()
                }
                return mInstance as AccountManager
            }
        }
    }

    private constructor()

    private var currentUser: User? = null
    lateinit var loginSharedPreferences: SharedPreferences

    fun init(context: Context) {
        loginSharedPreferences = context.getSharedPreferences(LOGIN_INFO, Context.MODE_PRIVATE)
    }

    fun isFirstOpen(): Boolean {
        val value = loginSharedPreferences.getBoolean(IS_FIRST_LOGIN, true)
        return loginSharedPreferences.getBoolean(IS_FIRST_LOGIN, true)
    }

    // 异步真正提交到硬件磁盘,
    // 而commit是同步的提交到硬件磁盘
    //addAccount(account);
    var currentAccount: String?
        get() {
            val value = loginSharedPreferences.getString(CURRENT_ACCOUNT, null)
            return value
        }
        set(account) {
            val editor = loginSharedPreferences.edit()
            editor.putString(CURRENT_ACCOUNT, account).apply()
        }

    var isAutoLogin: Boolean
        get() {
            val value = loginSharedPreferences.getBoolean(IS_AUTO_LOGIN, true)
            return loginSharedPreferences.getBoolean(IS_AUTO_LOGIN, true)
        }
        set(value) {
            val editor = loginSharedPreferences.edit()
            editor.putBoolean(IS_AUTO_LOGIN, value).apply()
        }

    var signKey: String?
        get() {
            val value = "Bearer " + loginSharedPreferences.getString(SIGN_KEY, "")
            return "Bearer " + loginSharedPreferences.getString(SIGN_KEY, "")

        }
        set(signKey) {
            val editor = loginSharedPreferences!!.edit()
            editor.putString(SIGN_KEY, signKey).apply()
        }

    val bleAddress: String
        get() = loginSharedPreferences.getString("bleAddress", "")

    fun putFirstOpen(isFirstLogin: Boolean) {
        val editor = loginSharedPreferences.edit()
        editor.putBoolean(IS_FIRST_LOGIN, isFirstLogin).apply()
    }


    fun setCurrentUser(user: User?) {
        if (user != null) {
            val account = user.userId
            currentAccount = user.userId
            setPassword(account, user.password)
            signKey = user.signKey
            UserDBManager.getInstance().insertUser(user)
            currentUser = getCurrentUser()
        }
    }


    fun getCurrentUser(): User? {
        if (currentUser == null) {
            val account = currentAccount
            if (!TextUtils.isEmpty(account))
                currentUser = getUser(account)
        }
        return currentUser
    }

    fun getUser(account: String?): User? {
        val user = UserDBManager.getInstance().getUser(account)
        if (user != null) {
            user.password = getPassword(account)
            user.signKey = signKey
        }
        return user
    }

    fun clearUser(account: String) {
        currentUser = null
        setPassword(account, "")
        signKey = ""
    }

    fun saveUserEncrypt(user: User) {
        val beforeEncrypt = JSON.toJSONString(user)
        Log.e(TAG, "加密前 -> $beforeEncrypt")
        try {
            val k = AESCipher.aesEncryptString(beforeEncrypt, "abcdabcdabcdabcd")
            Log.e(TAG, "加密后 -> $k")
            loginSharedPreferences.edit().putString("userEncrypt", k).apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun getUserDecrypt() {
        val beforeDescrypt = loginSharedPreferences.getString("userEncrypt", "")
        Log.e(TAG, "解密前 -> " + beforeDescrypt!!)
        try {
            val k = AESCipher.aesDecryptString(beforeDescrypt, "abcdabcdabcdabcd")
            Log.e(TAG, "解密后 -> $k")
            val user = JSON.parseObject(k, User::class.java)
            user.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    fun setPassword(account: String?, password: String?) {
        val editor = loginSharedPreferences.edit()
        editor.putString(PASSWORD, password).apply()
    }

    fun getPassword(account: String?): String {
        val value = loginSharedPreferences.getString(PASSWORD, "")
        return loginSharedPreferences.getString(PASSWORD, "")
    }

    fun saveBleAddress(address: String) {
        loginSharedPreferences.edit().putString("bleAddress", address).apply()
    }

}
