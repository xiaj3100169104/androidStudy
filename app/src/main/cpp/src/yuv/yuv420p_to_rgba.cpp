#include "yuv420p_to_rgba.h"
#include "ImageUtil.h"

extern "C" JNIEXPORT void JNICALL Java_com_ndk_YuvUtil_yuv420p2rgba
        (JNIEnv *env, jclass type, jbyteArray yuv420p_, jint width, jint height, jbyteArray rgba_) {
    jbyte *yuv420p = env->GetByteArrayElements(yuv420p_, NULL);
    jbyte *rgba = env->GetByteArrayElements(rgba_, NULL);

    i420torgba(reinterpret_cast<const unsigned char *>(yuv420p), width, height, reinterpret_cast<unsigned char *>(rgba));

    env->ReleaseByteArrayElements(yuv420p_, yuv420p, 0);
    env->ReleaseByteArrayElements(rgba_, rgba, 0);
}
