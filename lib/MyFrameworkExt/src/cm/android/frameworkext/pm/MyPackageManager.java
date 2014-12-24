package cm.android.frameworkext.pm;

import android.content.Context;
import android.content.pm.IPackageDeleteObserver;
import android.content.pm.IPackageInstallObserver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.RemoteException;

/**
 * PackageManager适配器，添加了静默卸载安装功能（该功能需要系统权限签名）
 */
public final class MyPackageManager {

    /**
     * Flag parameter for {@link #deletePackage} to indicate that you don't want
     * to delete the package's data directory.
     */
    public static final int DONT_DELETE_DATA = 0x01;

    public static final int DELETE_DATA = 0x02;
    // public static final int DONT_DELETE_DATA =
    // PackageManager.DELETE_KEEP_DATA;
    // public static final int DELETE_DATA = PackageManager.DELETE_ALL_USERS;

    /**
     * Flag parameter for
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)} to
     * indicate that this package should be installed as forward locked, i.e.
     * only the app itself should have access to its code and non-resource
     * assets.
     */
    public static final int INSTALL_FORWARD_LOCK = PackageManager.INSTALL_FORWARD_LOCK;

    /**
     * Flag parameter for {@link #installPackage} to indicate that you want to
     * replace an already installed package, if one exists.
     */
    public static final int INSTALL_REPLACE_EXISTING = PackageManager.INSTALL_REPLACE_EXISTING;

    /**
     * Flag parameter for {@link #installPackage} to indicate that you want to
     * allow test packages (those that have set android:testOnly in their
     * manifest) to be installed.
     */
    public static final int INSTALL_ALLOW_TEST = PackageManager.INSTALL_ALLOW_TEST;

    /**
     * Return code for when package deletion succeeds. This is passed to the
     * {@link IPackageDeleteObserver} by {@link #deletePackage()} if the system
     * succeeded in deleting the package.
     */
    public static final int DELETE_SUCCEEDED = PackageManager.DELETE_SUCCEEDED;

    /**
     * Installation return code: this is passed to the
     * {@link IPackageInstallObserver} by
     * {@link #installPackage(android.net.Uri, IPackageInstallObserver, int)} on
     * success.
     */
    public static final int INSTALL_SUCCEEDED = PackageManager.INSTALL_SUCCEEDED;

    private android.content.pm.PackageManager mPm = null;

    public MyPackageManager(Context context) {
        mPm = context.getPackageManager();
    }

    /**
     * Install a package. Since this may take a little while, the result will be
     * posted back to the given observer. An installation will fail if the
     * calling context lacks the
     * {@link android.Manifest.permission#INSTALL_PACKAGES} permission, if the
     * package named in the package file's manifest is already installed, or if
     * there's no space available on the device.
     *
     * @param packageURI           The location of the package file to install. This can be a
     *                             'file:' or a 'content:' URI.
     * @param observer             An observer callback to get notified when the package
     *                             installation is complete.
     *                             {@link IPackageInstallObserver#packageInstalled(String, int)}
     *                             will be called when that happens. observer may be null to
     *                             indicate that no callback is desired.
     * @param flags                - possible values: {@link #INSTALL_FORWARD_LOCK},
     *                             {@link #INSTALL_REPLACE_EXISTING}, {@link #INSTALL_ALLOW_TEST}
     *                             .
     * @param installerPackageName Optional package name of the application that is performing
     *                             the installation. This identifies which market the package
     *                             came from.
     */
    public void installPackage(final Uri packageURI,
            final IPackageInstallObserver observer, final int flags,
            final String installerPackageName) {
        mPm.installPackage(packageURI, observer, flags, installerPackageName);
    }

    /**
     * Attempts to delete a package. Since this may take a little while, the
     * result will be posted back to the given observer. A deletion will fail if
     * the calling context lacks the
     * {@link android.Manifest.permission#DELETE_PACKAGES} permission, if the
     * named package cannot be found, or if the named package is a
     * "system package". (TODO: include pointer to documentation on
     * "system packages")
     *
     * @param packageName The name of the package to delete
     * @param observer    An observer callback to get notified when the package deletion
     *                    is complete.
     *                    {@link android.content.pm.IPackageDeleteObserver#packageDeleted(boolean)}
     *                    will be called when that happens. observer may be null to
     *                    indicate that no callback is desired.
     * @param flags       - possible values: {@link #DONT_DELETE_DATA}
     */
    public void deletePackage(String packageName,
            IPackageDeleteObserver observer, int flags) {
        mPm.deletePackage(packageName, observer, flags);
    }
}

abstract class PackageInstallObserver extends IPackageInstallObserver.Stub {

    @Override
    public abstract void packageInstalled(String packageName, int returnCode);
}

abstract class PackageDeleteObserver extends IPackageDeleteObserver.Stub {

    @Override
    public abstract void packageDeleted(String packageName, int returnCode)
            throws RemoteException;
}
