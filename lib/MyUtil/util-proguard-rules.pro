#一般选项
#输出更多信息，如果处理过程中报错，会输出跟踪错误的所有信息
-verbose
#打印出所有的关于错误引用或任何重要的警告，但是proguard继续处理
-ignorewarnings

#不预校验
-dontpreverify

#初始参数
#混淆时不会产生大小写混合的类名
-dontusemixedcaseclassnames
#指定不去忽略非公共的库类。在4.5版本中是默认配置。
-dontskipnonpubliclibraryclasses

#保留源码的行号、源文件信息
-renamesourcefileattribute Source

#保留给定的可选属性，例如LineNumberTable, LocalVariableTable, SourceFile, Deprecated, Synthetic, Signature, and InnerClasses.
-keepattributes SourceFile,LineNumberTable
#保留注解:
-keepattributes *Annotation*
#避免使用泛型的位置混淆后出现类型转换错误:
-keepattributes Signature

#混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*

#优化
#不优化输入的类文件
#-dontoptimize
#优化：设置混淆的压缩比率 0 ~ 7 
-optimizationpasses 5
#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#所有activity的子类不要去混淆
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class * extends android.view.View

-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService

-keepclassmembers class * extends android.content.Context {  
   public void *(android.view.View);  
   public void *(android.view.MenuItem);  
}

# For nativeimpl methods, see http://proguard.sourceforge.net/manual/examples.html#nativeimpl
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
#保留枚举类型成员的方法：
-keepclassmembers,allowoptimization enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#保留资源文件
-keepclassmembers class **.R$* {  
    public static <fields>;  
}

#保留View子类读取XML的构造方法：
-keep public class * extends android.view.View {  
    public <init>(android.content.Context);  
    public <init>(android.content.Context, android.util.AttributeSet);  
    public <init>(android.content.Context, android.util.AttributeSet, int);  
    public void set*(...);  
}
-keepclasseswithmembers class * {
  public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#保留WebView中定义的与JS交互的类:
-keepattributes JavascriptInterface
-keep public class com.mypackage.MyClass$MyJavaScriptInterface
-keep public class * implements com.mypackage.MyClass$MyJavaScriptInterface
-keepclassmembers class com.mypackage.MyClass$MyJavaScriptInterface { 
    <methods>; 
}

# 保留WebView中定义的与JS交互的类:
-keepattributes JavascriptInterface
-keep public class com.mypackage.MyClass$MyJavaScriptInterface
-keep public class * implements com.mypackage.MyClass$MyJavaScriptInterface
-keepclassmembers class com.mypackage.MyClass$MyJavaScriptInterface { 
    <methods>; 
}

#保留JSON、Parcelable、Serailizable相关API:
-keepclassmembers class * {
   public <init>(org.json.JSONObject);
}
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements java.io.Serializable {  
    static final long serialVersionUID;  
    private static final java.io.ObjectStreamField[] serialPersistentFields;  
    private void writeObject(java.io.ObjectOutputStream);  
    private void readObject(java.io.ObjectInputStream);  
    java.lang.Object writeReplace();  
    java.lang.Object readResolve();  
}

-keep class * extends java.lang.annotation.Annotation { *; }

#去除调试日志:
#-assumenosideeffects class android.util.Log {
#  public static *** d(...); 
#  public static *** i(...);
#}

#================================框架======================================

# Keep our interfaces so they can be used by other ProGuard rules.
-keep,allowobfuscation @interface cm.java.proguard.annotations.Keep
-keep,allowobfuscation @interface cm.java.proguard.annotations.KeepAll
-keep,allowobfuscation @interface cm.java.proguard.annotations.KeepGettersAndSetters

# Do not strip any method/class that is annotated with @DoNotStrip
-keep @cm.java.proguard.annotations.Keep class *

-keep @cm.java.proguard.annotations.KeepAll class * { *; }

-keepclassmembers class * {
    @cm.java.proguard.annotations.Keep *;
}

-keepclassmembers @cm.java.proguard.annotations.KeepGettersAndSetters class * {
  void set*(***);
  *** get*();
}

#保留class名字的时候同时混淆该class
#-keepnames class cm.java.thread.ThreadPool

-dontwarn android.support.**
-keep class * extends android.support.**
-keep class * extends android.app.**
-keep class android.support.** { *; }

-keep class ch.qos.** { *; }
-keep class org.slf4j.** { *; }
-dontwarn ch.qos.logback.core.net.*
-assumenosideeffects class org.slf4j.Logger {
    public void debug(...);
}

#================================框架======================================