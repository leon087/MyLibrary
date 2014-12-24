package cm.android.frameworkext.pm;

import android.content.Context;
import android.net.Uri;

public final class PackageInstaller {

    private MyPackageManager mPm = null;

    private Context mContext = null;

    public PackageInstaller(Context context) {
        mPm = new MyPackageManager(context);
        mContext = context;
    }

    /**
     * 安装
     *
     * @param packageURI        apk包对应的uri
     * @param iPackageLisntener 安装状态监听器
     */
    public void installPackage(Uri packageURI,
            IPackageLisntener iPackageLisntener) {
        int flag = MyPackageManager.INSTALL_REPLACE_EXISTING;
        PackageInstallObserver observer = PackageInstallerFactory
                .createInstallObserver(iPackageLisntener);
        mPm.installPackage(packageURI, observer, flag,
                mContext.getPackageName());
    }

    /**
     * 卸载
     *
     * @param packageName       卸载应用包名
     * @param iPackageLisntener 卸载状态监听器
     */
    public void deletePackage(String packageName,
            IPackageLisntener iPackageLisntener) {
        PackageDeleteObserver observer = PackageInstallerFactory
                .createDeleteObserver(iPackageLisntener);
        int flag = MyPackageManager.DELETE_DATA;
        mPm.deletePackage(packageName, observer, flag);
    }

    /**
     * 安装卸载状态监听器
     */
    public interface IPackageLisntener {

        /**
         * 安装
         */
        public static final int TYPE_INSTALL = 0x01;
        /**
         * 卸载
         */
        public static final int TYPE_UNINSTALL = 0x02;

        /**
         * 安装/卸载成功
         *
         * @param type        参考{@link TYPE_INSTALL}和{@link TYPE_UNINSTALL}
         * @param packageName 应用包名
         * @param returnCode  返回状态
         */
        void onPackageSucceed(int type, String packageName, int returnCode);

        /**
         * 安装/卸载失败
         *
         * @param type        参考{@link TYPE_INSTALL}和{@link TYPE_UNINSTALL}
         * @param packageName 应用包名
         * @param returnCode  返回状态
         */
        void onPackageFailed(int type, String packageName, int returnCode);
    }
}
