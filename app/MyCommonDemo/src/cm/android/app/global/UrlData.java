package cm.android.app.global;

import cm.android.custom.MainConfig;
import cm.android.util.util.MapUtil;

import java.util.HashMap;
import java.util.Map;

public class UrlData {
	/** 获得地址列表 */
	public static final String URL_INIT = MainConfig.SERVER_ROOT
			+ "/portal-ott/ott/system/init.jsp";
	public static final String URL_INIT_ENCRYPT = MainConfig.SERVER_ROOT
			+ "/init.jsp";

	private Map<String, String> urls = new HashMap<String, String>();

	public void initUrl(Map<String, String> urls) {
		if (null != urls) {
			this.urls.putAll(urls);
			this.urls.clear();
		}
	}

	/** 从服务器下发的地址列表中获取接口地址 */
	public String getUrl(String key) {
		String url = MapUtil.getString(urls, key);
		return url;
	}

	/** 从map中获取接口地址 */
	public String getUrl(Map<String, Object> map, String key) {
		return MapUtil.getString(map, key);
	}

	public static final String MAIN_PAGE = "mainPage";

}
