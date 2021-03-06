package cm.android.common.pm;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.Uri;

import cm.android.applications.AppUtil;
import cm.java.thread.ThreadUtil;
import cm.java.util.Singleton;

import static okhttp3.internal.Internal.instance;

/**
 * PackageManager适配器
 */
public final class MyPackageManager {

    private static final Singleton<MyPackageManager> singleton = new Singleton<MyPackageManager>() {
        @Override
        protected MyPackageManager create() {
            return new MyPackageManager();
        }
    };

    public static MyPackageManager getInstance() {
        return singleton.get();
    }

    private MyPackageManager() {
    }

    public void installPackage(final Context context, final Uri packageURI, final InstallObserver observer) {
        ThreadUtil.newCachedThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                int returnCode = PackageUtil.install(context,
                        packageURI.getPath());

                if (null != observer) {
                    // 获取安装包信息
                    String archiveFilePath = packageURI.getPath();
                    PackageInfo packageInfo = AppUtil.getArchiveInfo(context.getPackageManager(), archiveFilePath, 0);
                    String packageName = "";
                    if (packageInfo != null) {
                        packageName = packageInfo.packageName;
                    }
                    observer.packageInstalled(packageName, returnCode);
                }
            }
        });
    }

    public void deletePackage(final Context context, final String packageName, final DeleteObserver observer) {
        ThreadUtil.newCachedThreadPool().submit(new Runnable() {
            @Override
            public void run() {
                int returnCode = PackageUtil.uninstall(context, packageName);
                if (null != observer) {
                    observer.packageDeleted(packageName, returnCode);
                }
            }
        });
    }
}

abstract class InstallObserver {

    public abstract void packageInstalled(String packageName, int returnCode);
}

abstract class DeleteObserver {

    public abstract void packageDeleted(String packageName, int returnCode);
}
