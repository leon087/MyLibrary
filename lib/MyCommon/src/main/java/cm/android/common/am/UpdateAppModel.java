package cm.android.common.am;

import cm.java.proguard.annotations.KeepAll;

@KeepAll
public class UpdateAppModel {

    public String packageName;

    public int versionCode;

    @Override
    public String toString() {
        return "UpdateAppModel{" +
                "packageName='" + packageName + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }
}
