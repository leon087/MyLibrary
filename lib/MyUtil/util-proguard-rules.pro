#================================util======================================
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

-keep class android.app.** {*;}
-keep class android.content.** {*;}
-keep class * implements android.os.IInterface {*;}
-keep class * extends android.os.IInterface {*;}

#-keep class ch.qos.** { *; }
#-keep class org.slf4j.** { *; }
#-dontwarn ch.qos.logback.core.net.*
#-assumenosideeffects class org.slf4j.Logger {
#    public void debug(...);
#}

#================================util======================================