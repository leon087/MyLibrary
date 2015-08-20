package cm.android.applications;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.test.InstrumentationTestCase;

import java.util.List;
import java.util.Map;

public class AppUtilTest extends InstrumentationTestCase {

    public void testGetInstalledPackages() throws Exception {
        PackageManager packageManager = getInstrumentation().getContext().getPackageManager();
        List<PackageInfo> packageInfoList = AppUtil.getInstalledPackages(packageManager);
        boolean result = packageInfoList.isEmpty();
        assertEquals(false, result);
    }

    public void testGetInstalledApplications() throws Exception {
        PackageManager packageManager = getInstrumentation().getContext().getPackageManager();
        List<ApplicationInfo> applicationInfoList = AppUtil
                .getInstalledApplications(packageManager);
        boolean result = applicationInfoList.isEmpty();
        assertEquals(false, result);
    }

    public void testGetInstalledApps() throws Exception {
        PackageManager packageManager = getInstrumentation().getContext().getPackageManager();
        List<ApplicationInfo> applicationInfoList = AppUtil
                .getInstalledApps(packageManager, 1);
        boolean result = applicationInfoList.isEmpty();
        assertEquals(false, result);
    }

    public void testGetInstalledAppsMap() throws Exception {
        PackageManager packageManager = getInstrumentation().getContext().getPackageManager();
        Map<String, ApplicationInfo> applicationInfoMap = AppUtil
                .getInstalledAppsMap(packageManager, 1);
        boolean result = applicationInfoMap.isEmpty();
        assertEquals(false, result);
    }

    public void testGetInstalledPackagesMap() throws Exception {
        PackageManager packageManager = getInstrumentation().getContext().getPackageManager();
        Map<String, PackageInfo> packageInfoMap = AppUtil
                .getInstalledPackagesMap(packageManager, 1);
        boolean result = packageInfoMap.isEmpty();
        assertEquals(false, result);
    }

    public void testGetLunchAppInfo() throws Exception {
        PackageManager packageManager = getInstrumentation().getContext().getPackageManager();
        List<ResolveInfo> resolveInfoList = AppUtil
                .getLunchAppInfo(packageManager);
        boolean result = resolveInfoList.isEmpty();
        assertEquals(false, result);
    }

    public void testGetPackageInfo() throws Exception {
        PackageManager packageManager = getInstrumentation().getContext().getPackageManager();
        String packageName = getInstrumentation().getContext().getPackageName();
        PackageInfo packageInfo = AppUtil.getPackageInfo(packageManager, packageName);
        boolean result = packageInfo == null;
        assertEquals(false, result);
    }

    public void testGetSignature() throws Exception {
        PackageManager packageManager = getInstrumentation().getContext().getPackageManager();
        String packageName = getInstrumentation().getContext().getPackageName();
        Signature[] signature = AppUtil.getSignature(packageManager, packageName);
        boolean result = signature.length == 0;
        assertEquals(false, result);
    }

    public void testGetSignatureHashCode() throws Exception {
        PackageManager packageManager = getInstrumentation().getContext().getPackageManager();
        String packageName = getInstrumentation().getContext().getPackageName();
        int temp = AppUtil.getSignatureHashCode(packageManager, packageName);
        boolean result = temp == 0;
        assertEquals(false, result);
    }

    public void testGetFingerprint() throws Exception {
        Context context = getInstrumentation().getContext();
        byte[] temp = AppUtil.getFingerprint(context, "123");
        boolean result = new String(temp).equals(new String(new byte[]{0x0}));
        assertEquals(false, result);
    }

    public void testIsSystemApp() throws Exception {
        PackageManager temp = getInstrumentation().getContext().getPackageManager();
        boolean result = AppUtil.isSystemApp(temp, "ddddd");
        assertEquals(false, result);
    }

    public void testIsAppInstalled() throws Exception {
        PackageManager packageManager = getInstrumentation().getContext().getPackageManager();
        String packageName = getInstrumentation().getContext().getPackageName();
        boolean result = AppUtil.isAppInstalled(packageManager, packageName);
        assertEquals(true, result);
    }
}
