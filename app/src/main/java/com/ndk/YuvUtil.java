package com.ndk;

public class YuvUtil {

    static {
        System.loadLibrary("yuv420p_to_rgba");
    }

    public static native void yuv420p2rgba(byte[] yuv420p, int width, int height, byte[] rgba);
}
