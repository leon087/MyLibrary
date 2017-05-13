# LeakCanary
-keep class org.eclipse.mat.** { *; }
-keep class com.squareup.leakcanary.** { *; }

# slf4j-android
-dontwarn org.apache.commons.compress.**
-keep class org.slf4j.** { *; }
