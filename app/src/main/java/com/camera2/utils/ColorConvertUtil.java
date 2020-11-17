package com.camera2.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;

import com.ndk.YuvUtil;

import java.nio.ByteBuffer;

public class ColorConvertUtil {

    private static final String TAG = "ColorConvertUtil";

    public static Bitmap yuv420pToBitmap(byte[] yuv420p, int width, int height) {
        if (yuv420p == null || width < 0 || height < 0) {
            Log.e(TAG, "cropNv21ToBitmap failed: illegal para !");
            return null;
        }
        byte[] rgba = new byte[width * height * 4];
        yuv420pToRGBA(yuv420p, width, height, rgba);
        Bitmap bitmap = byteArrayToBitmap(rgba, width, height);
        return bitmap;
    }

    public static void yuv420pToRGBA(byte[] yuv420p, int width, int height, byte[] rgba) {
        if (yuv420p == null || rgba == null) {
            Log.e(TAG, "yuv420pToRGBA failed: yuv420p or rgba is null ");
            return;
        }
        if (yuv420p.length != width * height * 3 / 2) {
            Log.e(TAG, "yuv420p length: " + yuv420p.length);
            Log.e(TAG, "yuv420pToRGBA failed: yuv420p length error!");
            return;
        }
        YuvUtil.yuv420p2rgba(yuv420p, width, height, rgba);
    }

    /**
     * 将 rgba 的 byte[] 数据转换成 bitmap
     *
     * @param rgba   输入的 rgba 数据
     * @param width  图片宽度
     * @param height 图片高度
     * @return 得到的 bitmap
     */
    public static Bitmap byteArrayToBitmap(byte[] rgba, int width, int height) {
        ByteBuffer buffer = ByteBuffer.wrap(rgba);
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        return bitmap;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int rotate, boolean mirrorX) {
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        if (mirrorX) {
            matrix.postScale(-1f, 1f);
        }
        Bitmap rotateBitmap = null;
        if (bitmap != null) {
            rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
            bitmap.recycle(); // 回收旧Bitmap
        }
        return rotateBitmap;
    }

    public static Bitmap rgb8888toRGB565(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] bitmap_data = new int[width * height];
        int[] screenByte = new int[width * height];
        bitmap.getPixels(bitmap_data, 0, width, 0, 0, width, height);
        for (int i = 0; i < width * height; i++) {
            short rgb565 = (short) RGB888ToRGB565(bitmap_data[i]);
            screenByte[i] = rgb565;
        }
        Bitmap b = Bitmap.createBitmap(screenByte, width, height, Bitmap.Config.RGB_565);
        return b;
    }

    public static byte[] bitmap2RGB(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] bitmap_data = new int[width * height];
        byte[] screenByte = new byte[width * height * 2];
        bitmap.getPixels(bitmap_data, 0, width, 0, 0, width, height);
        for (int i = 0; i < width * height; i++) {
            short rgb565 = (short) RGB888ToRGB565(bitmap_data[i]);
            shortToByteArray(rgb565, screenByte, i * 2);
        }
        return screenByte;
    }

    public static int shortToByteArray(short i, byte[] data, int offset) {
        data[offset + 1] = (byte) (i >> 8 & 255);
        data[offset] = (byte) (i & 255);
        return offset + 2;
    }

    public static int RGB888ToRGB565(int rgb8888) {
        return (rgb8888 >> 19 & 31) << 11 | (rgb8888 >> 10 & 63) << 5 | rgb8888 >> 3 & 31;
    }

}
