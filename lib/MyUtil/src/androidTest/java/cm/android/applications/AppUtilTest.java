package cm.android.applications;


import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.test.InstrumentationTestCase;

import java.util.List;
import java.util.Map;

public class AppUtilTest extends InstrumentationTestCase {

    public void testGetInstalledPackages() throws Exception {
        Context context = getInstrumentation().getContext();
        List<PackageInfo> packages = AppUtil.getInstalledPackages(context.getPackageManager());
        if (!packages.isEmpty()) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
    }

    public void testGetInstalledApplications() throws Exception {
        Context context = getInstrumentation().getContext();
        List<ApplicationInfo> packages = AppUtil
                .getInstalledApplications(context.getPackageManager());
        if (!packages.isEmpty()) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
    }

    public void testGetInstalledApps1() throws Exception {
        Context context = getInstrumentation().getContext();
        List<ApplicationInfo> packages = AppUtil.getInstalledApps(context.getPackageManager(), 0);
        if (!packages.isEmpty()) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
    }

    public void testGetInstalledApps2() throws Exception {
        Context context = getInstrumentation().getContext();
        List<ApplicationInfo> packages = AppUtil.getInstalledApps(context.getPackageManager(), 1);
        if (!packages.isEmpty()) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
    }

    public void testGetInstalledAppsMap() throws Exception {
        Context context = getInstrumentation().getContext();
        Map<String, ApplicationInfo> packages = AppUtil
                .getInstalledAppsMap(context.getPackageManager(), 0);
        if (!packages.isEmpty()) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
    }

    public void testGetArchiveInfo() throws Exception {
        Context context = getInstrumentation().getContext();
        String archiveFilePath = "we";
        PackageInfo packageInfo = AppUtil.getArchiveInfo(
                context.getPackageManager(), archiveFilePath);
        if (packageInfo == null) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
    }

    public void testGetLunchAppInfo() throws Exception {
        Context context = getInstrumentation().getContext();
        List<ResolveInfo> temp = AppUtil.getLunchAppInfo(context.getPackageManager());
        if (temp.isEmpty()) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetPackageInfo() throws Exception {
        Context context = getInstrumentation().getContext();
        PackageInfo packageInfo = AppUtil.getPackageInfo(context.getPackageManager(), "ace");
        if (packageInfo == null) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
    }

    public void testGetSignature() throws Exception {
        Context context = getInstrumentation().getContext();
        android.content.pm.Signature[] sigs = AppUtil
                .getSignature(context.getPackageManager(), context.getPackageName());
        if (sigs.length == 0) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetSignatureHashCode() throws Exception {
        Context context = getInstrumentation().getContext();
        int sig = 0;
        sig = AppUtil.getSignatureHashCode(context.getPackageManager(),
                context.getPackageName());
        if (sig == 0) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testIsSystemApp1() throws Exception {
        Context context = getInstrumentation().getContext();
        boolean sig = AppUtil.isSystemApp(context.getPackageManager(),
                context.getPackageName());
        assertEquals(sig, false);
    }


    public void testIsSystemApp2() throws Exception {
        if ((1 & ApplicationInfo.FLAG_SYSTEM) > 0) {
            assertEquals(true, true);
        } else {
            assertEquals(true, false);
        }
    }

}
