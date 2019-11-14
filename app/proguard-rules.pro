-dontskipnonpubliclibraryclasses # 不忽略非公共的库类
-optimizationpasses 5            # 指定代码的压缩级别
-dontusemixedcaseclassnames      # 是否使用大小写混合
-dontpreverify                   # 混淆时是否做预校验
-verbose                         # 混淆时是否记录日志
-dontoptimize                    # 优化不优化输入的类文件

-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*  # 混淆时所采用的算法

#生成日志数据，gradle build时在本项目根目录输出
-printseeds proguardLog/keeps.txt            #未混淆的类和成员
-printusage proguardLog/unused.txt           #未被使用的代码
-printmapping proguardLog/mapping.txt        #混淆前后的映射
#主要保持：注解、泛型、枚举、序列化、json反射用到的实体、第三方库
#android系统组件、view子类、android.support包、资源类R、jni native方法
#保持哪些类不被混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends android.view.View

#如果有引用v4或者v7包，需添加
-dontwarn android.support.**
#-keep class android.support.** {*;}  #加这个就报内存错
-keep class * extends android.support.v4.**{*;}
-keep class * extends android.support.v7.**{*;}
#不混淆资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
 # 保持 native 方法不被混淆,会默认不混淆其类名
-keepclasseswithmembernames class * {
    native <methods>;
}
#-dontwarn com.xxx**              #忽略某个包的警告
-keepattributes *Annotation*     # 保持注解
-keepattributes Signature        #不混淆泛型
-keepnames class * implements java.io.Serializable #不混淆Serializable
-keep class * implements android.os.Parcelable {         # 保持 Parcelable 不被混淆
    public static final android.os.Parcelable$Creator *;
}
-keepclassmembers enum * {             # 保持枚举 enum 类不被混淆
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
### greenDAO 3
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties

# If you do not use SQLCipher:
-dontwarn org.greenrobot.greendao.database.**
# If you do not use RxJava:
-dontwarn rx.**

#Glide的混淆规则
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.module.AppGlideModule
-keep class com.bumptech.glide.GeneratedAppGlideModuleImpl

#保持eventbus
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }
#androidEventBud
-keep class org.simple.** { *; }
-keep interface org.simple.** { *; }
-keepclassmembers class * {
    @org.simple.eventbus.Subscriber <methods>;
}
#混淆第三方jar包，其中xxx为jar包名,注意保持依赖库中的不混淆
#-libraryjars libs/pinyin4j-2.5.0.jar #这个会与build.gradle里面重复
-keep class com.style.bean.**{*;}       #不混淆某个包内的所有文件