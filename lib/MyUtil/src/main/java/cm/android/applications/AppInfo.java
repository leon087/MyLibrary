package cm.android.applications;

import android.content.Intent;
import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class AppInfo implements Cloneable, Serializable, Comparable<AppInfo> {

    private static final long serialVersionUID = -4184316050942392000L;

    public String appName; // 应用程序标签

    public Drawable appIcon; // 应用程序图像

    public Intent intent; // 启动应用程序的Intent，一般是Action为Main和Category为Lancher的Activity

    public String packageName; // 应用程序所对应的包名

    public String versionName; // 应用程序所对应的包名

    public int versionCode; // 应用程序所对应的包名

    public String lastUpdateTime;

    public long cachesize; // 缓存大小

    public long datasize; // 数据大小

    public long codesieze; // 应用程序大小

    @Override
    public boolean equals(Object o) {
        AppInfo appInfo = (AppInfo) o;
        return (appInfo.packageName.equals(this.packageName));
    }

    @Override
    public int hashCode() {
        return packageName.hashCode();
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public int compareTo(AppInfo appInfo) {
        return this.appName.compareTo(appInfo.appName);
    }
}
