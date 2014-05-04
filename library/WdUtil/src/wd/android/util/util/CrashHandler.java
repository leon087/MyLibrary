package wd.android.util.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Properties;
import java.util.TreeSet;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类 来接管程序,并记录 发送错误报告.
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	/** 系统默认的UncaughtException处理类 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	/** 程序的Context对象 */
	private Context mContext;

	private static final boolean isSavedSdcard = true;

	/** 使用Properties来保存设备的信息和错误堆栈信息 */
	private Properties mDeviceCrashInfo = new Properties();
	private static final String VERSION_NAME = "versionName";
	private static final String VERSION_CODE = "versionCode";
	private static final String STACK_TRACE = "STACK_TRACE";
	/** 错误报告文件的扩展名 */
	private static final String CRASH_REPORTER_EXTENSION = ".cr";
	private static final String CRASH_FILE_NAME = "crash-";

	/** 保证只有一个CrashHandler实例 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return CrashHandlerHolder.INSTANCE;
	}

	private static final class CrashHandlerHolder {
		private static final CrashHandler INSTANCE = new CrashHandler();
	}

	/**
	 * 初始化,注册Context对象, 获取系统默认的UncaughtException处理器, 设置该CrashHandler为程序的默认处理器
	 * 
	 * @param ctx
	 */
	public void init(Context ctx) {
		mContext = ctx;
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		handleException(ex);
		if (mDefaultHandler != null) {
			// 让系统默认的异常处理器来处理
			mDefaultHandler.uncaughtException(thread, ex);
		}

		// if (!handleException(ex) && mDefaultHandler != null) {
		// // 如果用户没有处理则让系统默认的异常处理器来处理
		// mDefaultHandler.uncaughtException(thread, ex);
		// } else {
		// // Sleep一会后结束程序
		// try {
		// Thread.sleep(3000);
		// } catch (InterruptedException e) {
		// Log.e(TAG, "Error : ", e);
		// }
		// android.os.Process.killProcess(android.os.Process.myPid());
		// System.exit(10);
		// }
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 开发者可以根据自己的情况来自定义异常处理逻辑
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false
	 */
	private boolean handleException(final Throwable ex) {
		if (ex == null) {
			return true;
		}
		final String msg = ex.getLocalizedMessage();
		// 使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				if (null == mContext) {
					return;
				}

				// Toast.makeText(mContext, "程序出错啦:" + msg, Toast.LENGTH_LONG)
				// .show();
				// 收集设备信息
				collectCrashDeviceInfo(mContext);
				// 保存错误报告文件
				String crashFileName = saveCrashInfoToFile(ex);
				// saveCrashInfo(ex);
				// 发送错误报告到服务器
				sendCrashReportsToServer(mContext);
				Looper.loop();
			}

		}.start();
		// // 收集设备信息
		// collectCrashDeviceInfo(mContext);
		// // 保存错误报告文件
		// String crashFileName = saveCrashInfoToFile(ex);
		// // 发送错误报告到服务器
		// sendCrashReportsToServer(mContext);

		return true;
	}

	/**
	 * 在程序启动时候, 可以调用该函数来发送以前没有发送的报告
	 */
	public void sendPreviousReportsToServer() {
		new Thread() {
			@Override
			public void run() {
				if (null == mContext) {
					return;
				}
				// 发送错误报告到服务器
				sendCrashReportsToServer(mContext);
			}

		}.start();
	}

	/**
	 * 把错误报告发送给服务器,包含新产生的和以前没发送的.
	 * 
	 * @param ctx
	 */
	private void sendCrashReportsToServer(Context ctx) {
		String[] crFiles = getCrashReportFiles(ctx);
		if (crFiles != null && crFiles.length > 0) {
			TreeSet<String> sortedFiles = new TreeSet<String>();
			sortedFiles.addAll(Arrays.asList(crFiles));

			for (String fileName : sortedFiles) {
				File cr = new File(ctx.getFilesDir(), fileName);
				postReport(cr);
				cr.delete();// 删除已发送的报告
			}
		}
	}

	private void postReport(File file) {
		// TODO 使用HTTP Post 发送错误报告到服务器
	}

	/**
	 * 获取错误报告文件名
	 * 
	 * @param ctx
	 * @return
	 */
	private String[] getCrashReportFiles(Context ctx) {
		File filesDir = ctx.getFilesDir();
		FilenameFilter filter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.endsWith(CRASH_REPORTER_EXTENSION);
			}
		};
		return filesDir.list(filter);
	}

	/**
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return
	 */
	private String saveCrashInfoToFile(Throwable ex) {
		Writer info = new StringWriter();
		PrintWriter printWriter = new PrintWriter(info);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		String result = info.toString();
		printWriter.close();

		mDeviceCrashInfo.put(STACK_TRACE, result);
		String exceptionLog = getCrashInfo(ex);

		try {
			long timestamp = System.currentTimeMillis();
			String fileName = CRASH_FILE_NAME + mContext.getPackageName() + "-"
					+ MyFormatter.formatDate2(timestamp)
					+ CRASH_REPORTER_EXTENSION;

			OutputStream trace = null;
			if (isSavedSdcard) {
				File file = new File(Environment.getExternalStorageDirectory(),
						fileName);
				trace = new FileOutputStream(file, true);
			} else {
				trace = mContext.openFileOutput(fileName, Context.MODE_PRIVATE);
			}
			mDeviceCrashInfo.store(trace, "");
			trace.write(exceptionLog.getBytes());

			trace.flush();
			trace.close();
			return fileName;
		} catch (Exception e) {
			MyLog.e(
					"an error occured while writing report file...", e);
		}
		return null;
	}

	private String getCrashInfo(Throwable aException) {
		NumberFormat theFormatter = new DecimalFormat("#0.");
		// String theErrReport = "";
		StringBuilder errReport = new StringBuilder();

		errReport.append("\n" + mContext.getPackageName()
				+ " generated the following exception:\n");
		errReport.append(aException.toString() + "\n");

		// theErrReport += mContext.getPackageName()
		// + " generated the following exception:\n";
		// theErrReport += aException.toString() + "\n\n";

		// stack trace
		StackTraceElement[] theStackTrace = aException.getStackTrace();
		if (theStackTrace.length > 0) {
			errReport.append("--------- Stack trace ---------\n");

			// theErrReport += "--------- Stack trace ---------\n";
			for (int i = 0; i < theStackTrace.length; i++) {
				errReport.append(theFormatter.format(i + 1) + "\t"
						+ theStackTrace[i].toString() + "\n");

				// theErrReport += theFormatter.format(i + 1) + "\t"
				// + theStackTrace[i].toString() + "\n";
			}// for
				// theErrReport += "-------------------------------\n\n";
			errReport.append("------------------------------\n");
		}

		// if the exception was thrown in a background thread inside
		// AsyncTask, then the actual exception can be found with getCause
		Throwable theCause = aException.getCause();
		if (theCause != null) {
			errReport.append("----------- Cause -----------\n");
			errReport.append(theCause.toString() + "\n");

			// theErrReport += "----------- Cause -----------\n";
			// theErrReport += theCause.toString() + "\n\n";
			theStackTrace = theCause.getStackTrace();
			for (int i = 0; i < theStackTrace.length; i++) {
				// theErrReport += theFormatter.format(i + 1) + "\t"
				// + theStackTrace[i].toString() + "\n";
				errReport.append(theFormatter.format(i + 1) + "\t"
						+ theStackTrace[i].toString() + "\n");
			}// for
				// theErrReport += "-----------------------------\n\n";
			errReport.append("-----------------------------\n");
		}// if

		// app environment
		PackageManager pm = mContext.getPackageManager();
		PackageInfo pi;
		try {
			pi = pm.getPackageInfo(mContext.getPackageName(), 0);
		} catch (NameNotFoundException eNnf) {
			// doubt this will ever run since we want info about our own package
			pi = new PackageInfo();
			pi.versionName = "unknown";
			pi.versionCode = 69;
		}
		Date theDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH.mm.ss_zzz");
		errReport.append("-------- Environment --------\n");
		errReport.append("Time\t=" + sdf.format(theDate) + "\n");
		errReport.append("Device\t=" + Build.FINGERPRINT + "\n");

		// theErrReport += "-------- Environment --------\n";
		// theErrReport += "Time\t=" + sdf.format(theDate) + "\n";
		// theErrReport += "Device\t=" + Build.FINGERPRINT + "\n";
		try {
			Field theMfrField = Build.class.getField("MANUFACTURER");
			// theErrReport += "Make\t=" + theMfrField.get(null) + "\n";
			errReport.append("Make\t=" + theMfrField.get(null) + "\n");
		} catch (SecurityException e) {
		} catch (NoSuchFieldException e) {
		} catch (IllegalArgumentException e) {
		} catch (IllegalAccessException e) {
		}
		errReport.append("Model\t=" + Build.MODEL + "\n");
		errReport.append("Product\t=" + Build.PRODUCT + "\n");
		errReport.append("App\t=" + mContext.getPackageName() + ", version "
				+ pi.versionName + " (build " + pi.versionCode + ")\n");
		errReport.append("Locale\t="
				+ mContext.getResources().getConfiguration().locale
						.getDisplayName() + "\n");
		errReport.append("-----------------------------\n");
		errReport.append("END REPORT.");

		// theErrReport += "Model\t=" + Build.MODEL + "\n";
		// theErrReport += "Product\t=" + Build.PRODUCT + "\n";
		// theErrReport += "App\t=" + mContext.getPackageName() + ", version "
		// + pi.versionName + " (build " + pi.versionCode + ")\n";
		// theErrReport += "Locale\t="
		// + mContext.getResources().getConfiguration().locale
		// .getDisplayName() + "\n";
		// theErrReport += "-----------------------------\n\n";
		//
		// theErrReport += "END REPORT.";
		return errReport.toString();
	}

	/**
	 * 收集程序崩溃的设备信息
	 * 
	 * @param ctx
	 */
	public void collectCrashDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				mDeviceCrashInfo.put(VERSION_NAME,
						pi.versionName == null ? "not set" : pi.versionName);
				mDeviceCrashInfo.put(VERSION_CODE,
						String.valueOf(pi.versionCode));
			}
		} catch (NameNotFoundException e) {
			MyLog.e("Error while collect package info", e);
		}
		// 使用反射来收集设备信息.在Build类中包含各种设备信息,
		// 例如: 系统版本号,设备生产商 等帮助调试程序的有用信息
		// 具体信息请参考后面的截图
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				mDeviceCrashInfo.put(field.getName(),
						String.valueOf(field.get(null)));
				MyLog
						.d(field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				MyLog.e("Error while collect crash info", e);
			}

		}
	}
}