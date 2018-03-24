#include <jni.h>
#include <string>
#include<Android/log.h>
#include <string.h>
#include "logUtil.cpp"

using namespace std;
#define TAG "JniTest"

extern "C"
JNIEXPORT jstring JNICALL
Java_example_ndk_JniTest_stringFromJNI(JNIEnv *env, jobject /* this */)
{
    string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C"//使用c的方式命名接口，必须加这个，不然报错找不到实现方法
JNIEXPORT void JNICALL
Java_example_ndk_JniTest_testShort(JNIEnv *env, jobject,
                                          jshort s) {
    //printf("s=%hd", s);
    __android_log_print(ANDROID_LOG_ERROR, TAG, "s=%hd", s);
    LOGE(TAG, "s=%hd", s);

}

extern "C"
JNIEXPORT void JNICALL
Java_example_ndk_JniTest_testBasicDataType(JNIEnv *env, jobject ,
                                                  jshort s, jint i, jlong l, jfloat f, jdouble d, jchar c, jboolean z, jbyte b)
{
    //printf("s=%hd, i=%d, l=%ld, f=%f, d=%lf, c=%c, z=%c, b=%d", s, i, l, f, d, c, z, b);
    __android_log_print(ANDROID_LOG_ERROR, TAG, "s=%hd, i=%d, l=%ld, f=%f, d=%lf, c=%c, z=%d, b=%d", s, i, l, f, d, c, z, b);
}

extern "C"
JNIEXPORT void JNICALL
Java_example_ndk_JniTest_testJString(JNIEnv *env, jobject ,
                                            jstring j_str, jobject jobj1, jobject job2, jintArray j_int_arr)
{
    __android_log_print(ANDROID_LOG_ERROR, TAG, "j_str: %s\n", j_str);

    const char *c_str = NULL;
    //返回指向字符串的 UTF-8 字符数组的指针。该数组在被ReleaseStringUTFChars() 释放前将一直有效
    c_str = env->GetStringUTFChars(j_str, NULL);
    __android_log_print(ANDROID_LOG_ERROR, TAG, "c_str转码后: %s\n", c_str);

    if (c_str == NULL)
    {
        return; // memory out
    }
    //通知虚拟机平台相关代码无需再访问 utf。utf 参数是一个指针，可利用 GetStringUTFChars() 获得。
    env->ReleaseStringUTFChars(j_str, c_str);
    //printf("c_str: %s\n", (char*)c_str);
    __android_log_print(ANDROID_LOG_ERROR, TAG, "c_str: %s\n", c_str);

}
