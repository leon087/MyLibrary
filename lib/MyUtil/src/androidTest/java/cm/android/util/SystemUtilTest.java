package cm.android.util;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Debug;
import android.test.InstrumentationTestCase;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class SystemUtilTest extends InstrumentationTestCase {

    public void testIsTopActivity() throws Exception {
        Context context = getInstrumentation().getContext();
        boolean result = SystemUtil.isTopActivity(context);
        assertEquals(result, true);
    }

//    public void testGetTopActivityPackageName() throws Exception {
//        Context context = getInstrumentation().getContext();
//        String result = "";
//        result = SystemUtil.getTopActivityPackageName(context);//requires android.permission.GET_TASKS 权限
//        if (result.equals("")) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }

    public void testGetRunningProcess() throws Exception {
        Context context = getInstrumentation().getContext();
        List<ActivityManager.RunningAppProcessInfo> result;
        result = SystemUtil.getRunningProcess(context);
        if (result.isEmpty()) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

//    public void testGetActivities() throws Exception {
//        Context context = getInstrumentation().getContext();
//        ArrayList<String> result =new ArrayList<String>();
//        result = SystemUtil.getActivities(context);
//        if (result.isEmpty()) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }

    public void testGetRunningProcessMemoryInfo() throws Exception {
        Context context = getInstrumentation().getContext();
        String packageName = context.getPackageName();
        Debug.MemoryInfo result = SystemUtil.getRunningProcessMemoryInfo(context, packageName);
        if (result == null) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

    public void testGetRunningAppProcessInfo() throws Exception {
        Context context = getInstrumentation().getContext();
        String packageName = context.getPackageName();
        ActivityManager.RunningAppProcessInfo result = SystemUtil.getRunningAppProcessInfo(context, packageName);
        if (result == null) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

//    public void testIsRunningInEmulator() throws Exception {
//        boolean result = SystemUtil.isRunningInEmulator();
//        assertEquals(false,result);
//    }

    public void testIsRoot() throws Exception {
        boolean result = SystemUtil.isRoot();
        assertEquals(false, result);
    }

    public void testIsExecutable() throws Exception {
        File file = EnvironmentUtil.getExternalStorageDirectory();
        Process p = null;
        try {
            p = Runtime.getRuntime().exec("ls -l " + file.getPath());
            // 获取返回内容
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    p.getInputStream()));
            String str = in.readLine();
            if (str != null && str.length() >= 4) {
                char flag = str.charAt(3);
                if (flag == 's' || flag == 'x') {
                    assertEquals(true, true);
                }
            }
        } catch (IOException e) {
            assertEquals(true, false);
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
    }

//    public void testGetFirstRunningTaskInfo() throws Exception {
//        Context context = getInstrumentation().getContext();
//        ActivityManager.RunningTaskInfo result = SystemUtil.getFirstRunningTaskInfo(context);//requires android.permission.GET_TASKS 需要权限
//        if (result == null) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }

//    public void testGetTopActivityName() throws Exception {
//        Context context = getInstrumentation().getContext();
//        String result = "";
//        result = SystemUtil.getTopActivityName(context);//requires android.permission.GET_TASKS 需要权限
//        if (result.equals("")) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }

    public void testGetCurProcessName() throws Exception {
        Context context = getInstrumentation().getContext();
        String result = "";
        result = SystemUtil.getCurProcessName(context);
        if (result == null) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }

//    public void testGetTopPackageNameCompat() throws Exception {
//        Context context = getInstrumentation().getContext();
//        String result = "";
//        result = SystemUtil.getTopPackageNameCompat(context);//requires android.permission.GET_TASKS 需要权限
//        if (result == null) {
//            assertEquals(true, false);
//        } else {
//            assertEquals(true, true);
//        }
//    }

}
