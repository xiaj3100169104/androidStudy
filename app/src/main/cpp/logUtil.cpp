//
// Created by xiajun on 2017/6/8.
//

#define LOGD(tag, fmt, value)  __android_log_print(ANDROID_LOG_DEBUG, tag, fmt, value) // 定义LOGD类型
#define LOGI(tag, fmt, value)  __android_log_print(ANDROID_LOG_INFO, tag, fmt, value) // 定义LOGI类型
#define LOGW(tag, fmt, value)  __android_log_print(ANDROID_LOG_WARN, tag, fmt, value) // 定义LOGW类型
#define LOGE(tag, fmt, value)  __android_log_print(ANDROID_LOG_ERROR, tag, fmt, value) // 定义LOGE类型
#define LOGF(tag, fmt, value)  __android_log_print(ANDROID_LOG_FATAL, tag, fmt, value) // 定义LOGF类型