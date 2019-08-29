package com.style.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 * Created by xiajun on 2017/1/9.
 * 单向加密算法
 包含MD5和SHA,就是只能加密不能解密
 SHA(安全散列算法)
 */

public class MD5Encryptor {

    public static String md5(final String c) {
        MessageDigest md = null;
        if (md == null) {
            try {
                md = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        if (md != null) {
            if (!TextUtils.isEmpty(c)) {
                md.update(c.getBytes());
                return byte2hex(md.digest());
            }
        }
        return "";
    }

    public static String byte2hex(byte b[]) {
        String hs = "";
        String stmp;
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xff);
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
            if (n < b.length - 1)
                hs = (new StringBuffer(String.valueOf(hs))).toString();
        }

        return hs.toLowerCase();
    }

}
