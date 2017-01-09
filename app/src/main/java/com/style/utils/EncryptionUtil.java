package com.style.utils;

import android.text.TextUtils;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密工具类
 * Created by xiajun on 2017/1/9.
 */

public class EncryptionUtil {

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

    public static String string2Base64string(String string) {
        String value = "";
        if (!TextUtils.isEmpty(string))
            value = Base64.encodeToString(string.getBytes(), Base64.NO_WRAP);
        return value;
    }

    public static String base64string2string(String base64string) {
        String value = "";
        if (!TextUtils.isEmpty(base64string)) {
            byte[] bytes = Base64.decode(base64string, Base64.DEFAULT);
            value = new String(bytes);
        }
        return value;
    }
   /* CRLF 这个参数看起来比较眼熟，它就是Win风格的换行符，意思就是使用CR LF这一对作为一行的结尾而不是Unix风格的LF
    DEFAULT 这个参数是默认，使用默认的方法来加密
    NO_PADDING 这个参数是略去加密字符串最后的”=”
    NO_WRAP 这个参数意思是略去所有的换行符（设置后CRLF就没用了）
    URL_SAFE 这个参数意思是加密时不使用对URL和文件名有特殊意义的字符来作为加密字符，具体就是以-和_取代+和/*/
}
