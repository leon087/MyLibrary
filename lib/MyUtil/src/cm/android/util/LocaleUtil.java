package cm.android.util;

import android.content.Context;
import android.content.res.Configuration;
import android.util.DisplayMetrics;

import java.util.Locale;

/**
 * Local信息管理类
 */
public class LocaleUtil {
	public static String showLocale() {
		StringBuilder sb = new StringBuilder();
		sb.append("getDefault() == " + Locale.getDefault().toString() + "\n");
		sb.append("getCountry() == " + Locale.getDefault().getCountry() + "\n");
		sb.append("getDisplayCountry() == "
				+ Locale.getDefault().getDisplayCountry() + "\n");
		sb.append("getLanguage() == " + Locale.getDefault().getLanguage()
				+ "\n");
		sb.append("getDisplayLanguage() == "
				+ Locale.getDefault().getDisplayLanguage() + "\n");
		sb.append("getDisplayName() == " + Locale.getDefault().getDisplayName()
				+ "\n");
		sb.append("getVariant() == " + Locale.getDefault().getVariant() + "\n");
		sb.append("getDisplayVariant() == "
				+ Locale.getDefault().getDisplayVariant() + "\n");
		sb.append("--------\n");
		for (Locale locale : Locale.getAvailableLocales()) {
			sb.append(locale.getCountry() + "--" + locale.getLanguage() + "--"
					+ locale.getDisplayCountry() + "--"
					+ locale.getDisplayLanguage() + "--"
					+ locale.getDisplayName() + "\n");
		}
		String str = sb.toString();
		return str;
	}

	// public static String showLocale2() {
	// StringBuilder sb = new StringBuilder();
	// sb.append("DisplayCountry--Country--Language\n");
	// for (Locale locale : Locale.getAvailableLocales()) {
	// sb.append(locale.getDisplayCountry() + "--" + locale.getCountry()
	// + "--" + locale.getLanguage() + "\n");
	// }
	// String str = sb.toString();
	// return str;
	// }

	public static String showLocale2() {
		StringBuilder sb = new StringBuilder();
		// sb.append("Language\n");
		sb.append("getCountry,getDisplayCountry,getDisplayLanguage,getDisplayName,getDisplayVariant,getISO3Country,getISO3Language,getLanguage,getVariant\n");
		for (Locale locale : Locale.getAvailableLocales()) {
			sb.append(locale.getCountry() + ",");
			sb.append(locale.getDisplayCountry() + ",");
			sb.append(locale.getDisplayLanguage() + ",");
			sb.append(locale.getDisplayName() + ",");
			sb.append(locale.getDisplayVariant() + ",");
			sb.append(locale.getISO3Country() + ",");
			sb.append(locale.getISO3Language() + ",");
			sb.append(locale.getLanguage() + ",");
			sb.append(locale.getVariant() + ",\n");
		}
		String str = sb.toString();
		return str;
	}

	public static String showTest(Context context) {
		// 首先获得当前的语言或者国家：
		String able = "getCountry() == "
				+ context.getResources().getConfiguration().locale.getCountry()
				+ "\n";
		return able;
	}

	/**
	 * 更新语言
	 * 
	 * @param context
	 */
	public static void updateLanguage(Context context) {
		// 选择中文
		Configuration config = context.getResources().getConfiguration();
		DisplayMetrics dm = context.getResources().getDisplayMetrics();
		config.locale = Locale.SIMPLIFIED_CHINESE;
		context.getResources().updateConfiguration(config, dm);
	}

	public static String getLocale() {
		Locale locale = Locale.getDefault();
		if (locale != null) {
			String lo = locale.getLanguage();
			MyLog.i("getLocale " + lo);
			if (lo != null) {
				return lo.toLowerCase();
			}
		}
		return "en";
	}
}
