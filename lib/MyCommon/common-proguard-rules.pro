#================================框架======================================

-keep class com.j256.** { *; }
-keep class cm.android.custom.ui.** { *; }
-keep class cm.android.common.ui.** { *; }

-keep class cm.android.common.db.** { *; }
-keep class * extends cm.android.common.db.BaseBean { *; }
-keep class * extends cm.android.common.db.BaseDao { *; }

#================================框架======================================