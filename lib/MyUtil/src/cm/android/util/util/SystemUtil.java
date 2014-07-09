package cm.android.util.util;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Debug;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统环境Util类
 */
public class SystemUtil {
	/**
	 * 判断进程是否正在运行
	 * 
	 * @param ctx
	 * @param name
	 *            运行的进程名
	 * @return
	 */
	public static boolean isProcessRunning(Context ctx, String name) {
		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> apps = am.getRunningAppProcesses();
		for (RunningAppProcessInfo app : apps) {
			if (app.processName.equals(name)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isTopActivity(Context ctx) {
		ActivityManager am = (ActivityManager) ctx.getApplicationContext()
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningAppProcessInfo> list = am
				.getRunningAppProcesses();
		if (list != null && list.isEmpty())
			return false;
		for (ActivityManager.RunningAppProcessInfo process : list) {
			if (process.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND
					&& process.processName.equals(ctx.getPackageName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断Service是否正在运行
	 * 
	 * @param ctx
	 * @param serviceName
	 *            service名
	 * @param processName
	 *            该service所在进程名
	 * @return
	 */
	public static boolean isServiceRunning(Context ctx, String serviceName,
			String processName) {
		ActivityManager manager = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager
				.getRunningServices(Integer.MAX_VALUE)) {
			if (serviceName.equals(service.service.getClassName())
					&& processName.equals(service.process))
				return true;
		}
		return false;
	}

	/**
	 * 获取栈顶activity
	 */
	public static String getTopActivityPackageName(Context context) {
		List<RunningTaskInfo> taskInfos;
		// 判断程序是否处于桌面
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		taskInfos = am.getRunningTasks(1);
		String packageName = taskInfos.get(0).topActivity.getPackageName();
		return packageName;
	}

	/**
	 * 获取正在运行的进程列表
	 * 
	 * @param ctx
	 * @return
	 */
	public static List<RunningAppProcessInfo> getRunningProcess(Context ctx) {
		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> apps = am.getRunningAppProcesses();
		return apps;
	}

	/**
	 * 获取应用下所有Activity
	 * 
	 * @param ctx
	 * @return
	 */
	public static ArrayList<String> getActivities(Context ctx) {
		ArrayList<String> result = new ArrayList<String>();
		Intent intent = new Intent(Intent.ACTION_MAIN, null);
		intent.setPackage(ctx.getPackageName());
		for (ResolveInfo info : ctx.getPackageManager().queryIntentActivities(
				intent, 0)) {
			result.add(info.activityInfo.name);
		}
		return result;
	}

	public boolean isRunningInEmulator() {
		boolean qemuKernel = false;
		Process process = null;
		DataOutputStream os = null;
		try {
			process = Runtime.getRuntime().exec("get prop ro.kernel.qemu");
			os = new DataOutputStream(process.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(
					process.getInputStream(), "GBK"));
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
			qemuKernel = (Integer.valueOf(in.readLine()) == 1);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (os != null) {
					os.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return qemuKernel;
	}

	public static Debug.MemoryInfo getRunningProcessMemoryInfo(Context context,
			String packageName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list2 = am.getRunningAppProcesses();
		for (RunningAppProcessInfo ti : list2) {
			// if (ti.processName.equals("system")
			// || ti.processName.equals("com.android.phone")) {
			// continue;
			// }

			if (ti.processName.equals(packageName)) {
				int[] pids = new int[] { ti.pid };
				Debug.MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(pids);
				return memoryInfos[0];
			}
		}
		return null;
	}

	/**
	 * 根据processName获得RunningAppProcessInfo
	 * 
	 * @return
	 */
	public static RunningAppProcessInfo getRunningAppProcessInfo(
			Context context, String processName) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list2 = am.getRunningAppProcesses();
		for (RunningAppProcessInfo ti : list2) {
			if (ti.processName.equals(processName)) {
				return ti;
			}
		}
		return null;
	}
}
