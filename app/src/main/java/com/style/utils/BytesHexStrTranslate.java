package com.style.utils;

/**
 * 16进制数的字符串转字节数组。
 * 注意：16进制数的字符串都是两个字符,譬如010203090A0BFF
 */
public class BytesHexStrTranslate {

    /*
     * 16进制字符串转字节数组
     */
    public static byte[] hexString2Bytes(String hex) {
        if ((hex == null) || (hex.equals(""))) {
            return null;
        } else if (hex.length() % 2 != 0) {
            return null;
        } else {
            hex = hex.toUpperCase();
            int len = hex.length() / 2;
            byte[] bytes = new byte[len];
            char[] hc = hex.toCharArray();
            for (int i = 0; i < len; i++) {
                int p = 2 * i;
                bytes[i] = (byte) (charToByte(hc[p]) << 4 | charToByte(hc[p + 1]));
            }
            return bytes;
        }

    }

    /*
     * 字符转换为字节:asc码转换为数字
     */
    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    /*16进制数的byte数组转String*/
    public static String bytes2HexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        String hexString;
        for (int i = 0; i < bytes.length; i++) {
            hexString = Integer.toHexString(0xFF & bytes[i]);
            if (hexString.length() == 1)
                sb.append(0);
            sb.append(hexString.toUpperCase());
        }
        return sb.toString();
    }

}
