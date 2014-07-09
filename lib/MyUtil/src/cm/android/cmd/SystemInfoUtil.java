package cm.android.cmd;

import android.app.ActivityManager;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

public class SystemInfoUtil {

	// @Override
	// public void onCreate(Bundle savedInstanceState) {
	// super.onCreate(savedInstanceState);
	// setContentView(R.layout.main);
	// TextView txt = (TextView) findViewById(R.id.txt);
	// txt.setBackgroundColor(0xffffffff);
	// txt.setTextColor(0xff0000ff);
	// // txt.setText(getSystemProperty());//显示系统属性
	// // txt.setText(getMemoryInfo(this));//显示内存信息
	// // txt.setText(getVersionInfo());//显示系统版本信息
	// // txt.setText(getCPUInfo());//显示CPU信息
	// // txt.setText(getDiskInfo());//显示盘符信息
	// // txt.setText(getDmesgInfo());//显示dmesg信息
	// // txt.setText(getNetConfigInfo());//显示网络设置信息
	// // txt.setText(getNetStatusInfo());//显示网络状态信息
	// // txt.setText(getMountInfo());//显示Mount信息
	// txt.setText(getTelStatus(this));//显示电话网络信息
	// }
	/**
	 * System Property文件为：<br/>
	 * 1./default.prop <br/>
	 * 2./system/build.prop <br/>
	 * 3./system/default.prop <br/>
	 * 4./data/local.prop <br/>
	 * 属性信息按照上面的顺序被加载。后加载的属性会覆盖前面的属性值(注：当属性名称相同的时候)。当上面加载完成后，最后加载的是驻留属性，保存在/data
	 * /property文件中。<br/>
	 * 详见：http://blog.csdn.net/jerryutscn/archive/2010/04/24/5519423.aspx
	 */
	public static String getSystemProperty() {
		StringBuilder strBuf = new StringBuilder();
		strBuf.append("java.vendor.url="
				+ System.getProperty("java.vendor.url") + "\n");
		strBuf.append("java.class.path="
				+ System.getProperty("java.class.path") + "\n");
		strBuf.append("user.home=" + System.getProperty("user.home") + "\n");
		strBuf.append("java.class.version="
				+ System.getProperty("java.class.version") + "\n");
		strBuf.append("os.version=" + System.getProperty("os.version") + "\n");
		strBuf.append("java.vendor=" + System.getProperty("java.vendor") + "\n");
		strBuf.append("user.dir=" + System.getProperty("user.dir") + "\n");
		strBuf.append("user.timezone=" + System.getProperty("user.timezone")
				+ "\n");
		strBuf.append("path.separator=" + System.getProperty("path.separator")
				+ "\n");
		strBuf.append("os.name=" + System.getProperty("os.name") + "\n");
		strBuf.append("os.arch=" + System.getProperty("os.arch") + "\n");
		strBuf.append("line.separator=" + System.getProperty("line.separator")
				+ "\n");
		strBuf.append("file.separator=" + System.getProperty("file.separator")
				+ "\n");
		strBuf.append("user.name=" + System.getProperty("user.name") + "\n");
		strBuf.append("java.version=" + System.getProperty("java.version")
				+ "\n");
		strBuf.append("java.home=" + System.getProperty("java.home") + "\n");
		return strBuf.toString();
	}

	// 直接复制的反编译结果，未整理。
	public static String getDisplayMetrics(Context context) {
		DisplayMetrics displaymetrics1 = context.getApplicationContext()
				.getResources().getDisplayMetrics();
		int i = displaymetrics1.widthPixels;
		int j = displaymetrics1.heightPixels;
		float f = displaymetrics1.density;
		float f1 = displaymetrics1.xdpi;
		float f2 = displaymetrics1.ydpi;
		String s = String.valueOf("");
		StringBuilder stringbuilder = (new StringBuilder(s))
				.append("The absolute width:");
		String s1 = String.valueOf(i);
		String s2 = String.valueOf(stringbuilder.append(s1).append("pixels\n")
				.toString());
		StringBuilder stringbuilder1 = (new StringBuilder(s2))
				.append("The absolute heightin:");
		String s3 = String.valueOf(j);
		String s4 = String.valueOf(stringbuilder1.append(s3).append("pixels\n")
				.toString());
		StringBuilder stringbuilder2 = (new StringBuilder(s4))
				.append("The logical density of the display.:");
		String s5 = String.valueOf(f);
		String s6 = String.valueOf(stringbuilder2.append(s5).append("\n")
				.toString());
		StringBuilder stringbuilder3 = (new StringBuilder(s6))
				.append("X dimension :");
		String s7 = String.valueOf(f1);
		String s8 = String.valueOf(stringbuilder3.append(s7)
				.append("pixels per inch/n").toString());
		StringBuilder stringbuilder4 = (new StringBuilder(s8))
				.append("Y dimension :");
		String s9 = String.valueOf(f2);
		return stringbuilder4.append(s9).append("pixels per inch\n").toString();
	}

	public static String getVersionInfo() {
		String[] args = { "/system/bin/cat", "/proc/version" };
		return CmdExecute.run(args, "/system/bin/");
	}

	public static String getCPUInfo() {
		String[] args = { "/system/bin/cat", "/proc/cpuinfo" };
		return CmdExecute.run(args, "/system/bin/");
	}

	public static String getDiskInfo() {
		String[] args = { "/system/bin/df" };
		return CmdExecute.run(args, "/system/bin/");
	}

	public static String getDmesgInfo() {
		String[] args = { "/system/bin/dmesg" };
		return CmdExecute.run(args, "/system/bin/");
	}

	public static String getNetConfigInfo() {
		String[] args = { "/system/bin/netcfg" };
		return CmdExecute.run(args, "/system/bin/");
	}

	public static String getNetStatusInfo() {
		String[] args = { "/system/bin/netstat" };
		return CmdExecute.run(args, "/system/bin/");
	}

	public static String getMountInfo() {
		String[] args = { "/system/bin/mount" };
		return CmdExecute.run(args, "/system/bin/");
	}

	public static String getDumpsysMeminfo(String packageName) {
		String[] args = { "/system/bin/dumpsys", "meminfo", packageName };
		return CmdExecute.run(args, "/system/bin/");
	}

	public static String getDumpsysCpuinfo() {
		String[] args = { "/system/bin/dumpsys", "cpuinfo" };
		return CmdExecute.run(args, "/system/bin/");
	}

	public static String getDumpsysBattery() {
		String[] args = { "/system/bin/dumpsys", "battery" };
		return CmdExecute.run(args, "/system/bin/");
	}

	public static String getMemoryInfo(Context context) {
		StringBuilder strBuf = new StringBuilder();
		ActivityManager actMgr = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		android.app.ActivityManager.MemoryInfo memoryinfo = new android.app.ActivityManager.MemoryInfo();
		actMgr.getMemoryInfo(memoryinfo);
		strBuf.append("\nmemoryinfo.threshold = ");
		long threshold = memoryinfo.threshold >> 20;
		strBuf.append(threshold).append("M");
		strBuf.append("\nmemoryinfo.availMem = ");
		long availMem = memoryinfo.availMem >> 20;
		strBuf.append(availMem).append("M");
		strBuf.append("\nmemoryinfo.lowMemory = ");
		boolean lowMemory = memoryinfo.lowMemory;
		strBuf.append(lowMemory).append("\n");
		String[] args = { "/system/bin/cat", "/proc/meminfo" };
		strBuf.append(CmdExecute.run(args, "/system/bin/"));
		return strBuf.toString();
	}

	public static String getProcessRunningInfo() {
		String[] args = { "/system/bin/top", "-n", "1" };
		String result = CmdExecute.run(args, "/system/bin/");
		return result;
	}

	public static void killProcess(int pid) {
		// 没有权限
		String[] args = { "/system/bin/kill", String.valueOf(pid) };
		CmdExecute.run(args, "/system/bin/");
	}

	// 直接复制的反编译结果，未整理。
	public static String getTelStatus(Context context) {
		TelephonyManager telephonymanager = (TelephonyManager) context
				.getSystemService("phone");
		String s = String.valueOf("");
		StringBuilder stringbuilder = (new StringBuilder(s))
				.append("DeviceId(IMEI) = ");
		String s1 = telephonymanager.getDeviceId();
		String s2 = String.valueOf(stringbuilder.append(s1).append("\n")
				.toString());
		StringBuilder stringbuilder1 = (new StringBuilder(s2))
				.append("DeviceSoftwareVersion = ");
		String s3 = telephonymanager.getDeviceSoftwareVersion();
		String s4 = String.valueOf(stringbuilder1.append(s3).append("\n")
				.toString());
		StringBuilder stringbuilder2 = (new StringBuilder(s4))
				.append("Line1Number = ");
		String s5 = telephonymanager.getLine1Number();
		String s6 = String.valueOf(stringbuilder2.append(s5).append("\n")
				.toString());
		StringBuilder stringbuilder3 = (new StringBuilder(s6))
				.append("NetworkCountryIso = ");
		String s7 = telephonymanager.getNetworkCountryIso();
		String s8 = String.valueOf(stringbuilder3.append(s7).append("\n")
				.toString());
		StringBuilder stringbuilder4 = (new StringBuilder(s8))
				.append("NetworkOperator = ");
		String s9 = telephonymanager.getNetworkOperator();
		String s10 = String.valueOf(stringbuilder4.append(s9).append("\n")
				.toString());
		StringBuilder stringbuilder5 = (new StringBuilder(s10))
				.append("NetworkOperatorName = ");
		String s11 = telephonymanager.getNetworkOperatorName();
		String s12 = String.valueOf(stringbuilder5.append(s11).append("\n")
				.toString());
		StringBuilder stringbuilder6 = (new StringBuilder(s12))
				.append("NetworkType = ");
		int i = telephonymanager.getNetworkType();
		String s13 = String.valueOf(stringbuilder6.append(i).append("\n")
				.toString());
		StringBuilder stringbuilder7 = (new StringBuilder(s13))
				.append("PhoneType = ");
		int j = telephonymanager.getPhoneType();
		String s14 = String.valueOf(stringbuilder7.append(j).append("\n")
				.toString());
		StringBuilder stringbuilder8 = (new StringBuilder(s14))
				.append("SimCountryIso = ");
		String s15 = telephonymanager.getSimCountryIso();
		String s16 = String.valueOf(stringbuilder8.append(s15).append("\n")
				.toString());
		StringBuilder stringbuilder9 = (new StringBuilder(s16))
				.append("SimOperator = ");
		String s17 = telephonymanager.getSimOperator();
		String s18 = String.valueOf(stringbuilder9.append(s17).append("\n")
				.toString());
		StringBuilder stringbuilder10 = (new StringBuilder(s18))
				.append("SimOperatorName = ");
		String s19 = telephonymanager.getSimOperatorName();
		String s20 = String.valueOf(stringbuilder10.append(s19).append("\n")
				.toString());
		StringBuilder stringbuilder11 = (new StringBuilder(s20))
				.append("SimSerialNumber = ");
		String s21 = telephonymanager.getSimSerialNumber();
		String s22 = String.valueOf(stringbuilder11.append(s21).append("\n")
				.toString());
		StringBuilder stringbuilder12 = (new StringBuilder(s22))
				.append("SimState = ");
		int k = telephonymanager.getSimState();
		String s23 = String.valueOf(stringbuilder12.append(k).append("\n")
				.toString());
		StringBuilder stringbuilder13 = (new StringBuilder(s23))
				.append("SubscriberId(IMSI) = ");
		String s24 = telephonymanager.getSubscriberId();
		String s25 = String.valueOf(stringbuilder13.append(s24).append("\n")
				.toString());
		StringBuilder stringbuilder14 = (new StringBuilder(s25))
				.append("VoiceMailNumber = ");
		String s26 = telephonymanager.getVoiceMailNumber();
		String s27 = stringbuilder14.append(s26).append("\n").toString();
		int l = context.getResources().getConfiguration().mcc;
		int i1 = context.getResources().getConfiguration().mnc;
		String s28 = String.valueOf(s27);
		StringBuilder stringbuilder15 = (new StringBuilder(s28))
				.append("IMSI MCC (Mobile Country Code):");
		String s29 = String.valueOf(l);
		String s30 = String.valueOf(stringbuilder15.append(s29).append("\n")
				.toString());
		StringBuilder stringbuilder16 = (new StringBuilder(s30))
				.append("IMSI MNC (Mobile Network Code):");
		String s31 = String.valueOf(i1);
		return stringbuilder16.append(s31).append("\n").toString();
	}

	public static String getMacAddress() {
		String[] args = { "/system/bin/cat", "/sys/class/net/wlan0/address" };
		return CmdExecute.run(args, "/system/bin/");
	}
}
