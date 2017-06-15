package xiajun.example.ndk;

/**
 * Created by xiajun on 2017/6/7.
 */

public class JniTest {
    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }


    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    public static native String stringFromJNI();

    public static native void testShort(short s);
    public static native void testBasicDataType(short s, int i, long l, float f, double d, char c, boolean z, byte b);
    public static native void testJString(String str, Object obj, JniTestActivity.MyClass p, int[] arr);

}
