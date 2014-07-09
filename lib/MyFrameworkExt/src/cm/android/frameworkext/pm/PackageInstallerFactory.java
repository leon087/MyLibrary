package cm.android.frameworkext.pm;

import android.os.RemoteException;
import cm.android.frameworkext.pm.PackageInstaller.IPackageLisntener;

/**
 * 安装/卸载监听器创建工厂
 */
final class PackageInstallerFactory {
	static PackageInstallObserver createInstallObserver(
			IPackageLisntener iPackageLisntener) {
		if (null == iPackageLisntener) {
			return null;
		}
		return new InstallObserver(iPackageLisntener);
	}

	static PackageDeleteObserver createDeleteObserver(
			IPackageLisntener iPackageLisntener) {
		if (null == iPackageLisntener) {
			return null;
		}
		return new DeleteObserver(iPackageLisntener);
	}

	private static class InstallObserver extends PackageInstallObserver {
		private PackageInstaller.IPackageLisntener iPackageLisntener = null;

		InstallObserver(PackageInstaller.IPackageLisntener iPackageLisntener) {
			this.iPackageLisntener = iPackageLisntener;
		}

		@Override
		public void packageInstalled(String packageName, int returnCode) {
			if (returnCode == MyPackageManager.INSTALL_SUCCEEDED) {
				iPackageLisntener
						.onPackageSucceed(IPackageLisntener.TYPE_INSTALL,
								packageName, returnCode);
			} else {
				iPackageLisntener
						.onPackageFailed(IPackageLisntener.TYPE_INSTALL,
								packageName, returnCode);
			}
		}
	}

	private static class DeleteObserver extends PackageDeleteObserver {
		private PackageInstaller.IPackageLisntener iPackageLisntener = null;

		DeleteObserver(IPackageLisntener iPackageLisntener) {
			this.iPackageLisntener = iPackageLisntener;
		}

		@Override
		public void packageDeleted(String packageName, int returnCode)
				throws RemoteException {
			if (returnCode == MyPackageManager.DELETE_SUCCEEDED) {
				iPackageLisntener.onPackageSucceed(
						IPackageLisntener.TYPE_UNINSTALL, packageName,
						returnCode);
			} else {
				iPackageLisntener.onPackageFailed(
						IPackageLisntener.TYPE_UNINSTALL, packageName,
						returnCode);
			}
		}
	}
}
