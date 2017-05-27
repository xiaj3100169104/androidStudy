#include <jni.h>
#include <com_style_AndroidJniUtils.h>

#ifndef _Included_com_style_AndroidJniUtils
#define _Included_com_style_AndroidJniUtils
#ifdef __cplusplus
extern "C" {
#endif

JNIEXPORT jstring JNICALL Java_com_style_AndroidJniUtils_sayHello(JNIEnv *env, jobject jobj)
{

    return (*env)->NewStringUTF(env,"JNI hahahahahahahaha");//c
    //return (*env)->NewStringUTF("JNI hahahahahahahaha");//c++

}

#ifdef __cplusplus
}
#endif
#endif
