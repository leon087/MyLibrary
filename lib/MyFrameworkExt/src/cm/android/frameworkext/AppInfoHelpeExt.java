package cm.android.frameworkext;

import android.content.Context;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageManager;
import android.content.pm.PackageParser;
import android.content.pm.PackageStats;
import android.os.RemoteException;
import android.util.DisplayMetrics;

import java.io.File;
import java.lang.reflect.Method;

public class AppInfoHelpeExt {
    /**
     * 查询应用大小
     *
     * @param context
     * @param pkgName
     * @throws Exception
     */
    public static void queryPacakgeSize(Context context, String pkgName)
            throws Exception {
        if (pkgName == null) {
            return;
        }

        // 使用放射机制得到PackageManager类的隐藏函数getPackageSizeInfo
        PackageManager pm = context.getPackageManager(); // 得到pm对象
        try {
            // 通过反射机制获得该隐藏函数
            Method getPackageSizeInfo = pm.getClass().getDeclaredMethod(
                    "getPackageSizeInfo", String.class,
                    IPackageStatsObserver.class);
            // 调用该函数，并且给其分配参数 ，待调用流程完成后会回调PkgSizeObserver类的函数
            PackageStatsObserver packageStatsObserver = new PackageStatsObserver();
            getPackageSizeInfo.invoke(pm, pkgName, packageStatsObserver);
            // synchronized (packageStatsObserver) {
            // while (!packageStatsObserver.isFinished()) {
            // packageStatsObserver.wait();
            // }
            // }
            // return packageStatsObserver.getPackageStats();
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex; // 抛出异常
        }
    }

    /**
     * 获取PackageParser.Package对象
     *
     * @param apkFilePath
     * @return
     */
    public static PackageParser.Package getPackage(String apkFilePath) {
        // 这是一个Package 解释器, 是隐藏的
        // 构造函数的参数只有一个, apk文件的路径
        PackageParser packageParser = new PackageParser(apkFilePath);
        // 这个是与显示有关的, 里面涉及到一些像素显示等等, 我们使用默认的情况
        DisplayMetrics metrics = new DisplayMetrics();
        metrics.setToDefaults();
        // 这里就是解析了, 四个参数,
        // 源文件File,
        // 目的文件路径(这个我也没搞清楚怎么回事, 看Android安装器源码, 用的是源文件路径, 但名字却是destFileName)
        // 显示, DisplayMetrics metrics
        // flags, 这个真不知道是啥
        PackageParser.Package mPkgInfo = packageParser.parsePackage(new File(
                apkFilePath), apkFilePath, metrics, 0);
        return mPkgInfo;
    }

    /**
     * aidl文件形成的Bindler机制服务类
     */
    public static class PackageStatsObserver extends IPackageStatsObserver.Stub {
        private volatile boolean mIsFinished = false;
        private PackageStats mPackageStats = null;

        public boolean isFinished() {
            return mIsFinished;
        }

        public PackageStats getPackageStats() {
            return mPackageStats;
        }

        /**
         * 回调函数，
         *
         * @param pStatus   ,返回数据封装在PackageStats对象中
         * @param succeeded 代表回调成功
         */
        @Override
        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
                throws RemoteException {
            mPackageStats = pStats;
            mIsFinished = true;
            synchronized (this) {
                this.notifyAll();
            }
        }
    }
}
