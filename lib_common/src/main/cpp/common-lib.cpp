#include <jni.h>
#include <string>
#include<Android/log.h>
#include <string.h>

using namespace std;
#define TAG "JniCommon"

extern "C"
JNIEXPORT jstring JNICALL
Java_com_ndk_JniCommon_stringFromJNI(JNIEnv *env, jclass clazz) {
    string hello = "Hello from common-lib";
    __android_log_print(ANDROID_LOG_ERROR, TAG, "c_str");
    return env->NewStringUTF(hello.c_str());
}

