package wd.android.common.http;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Header;

import wd.android.common.cache.core.LocalCache;
import wd.android.common.cache.disk.entry.HttpCacheEntry;
import wd.android.common.cache.disk.local.HttpCacheLoder;
import wd.android.util.util.EnvironmentInfo;
import wd.android.util.util.MyLog;
import wd.android.util.util.Utils;
import android.content.Context;

/**
 * 带有缓存的数据请求接口
 */
public class HttpLoader {
	// 后续会替换掉
	// public static GuavaCache<HttpCacheEntry> guavaCache;
	public static LocalCache localCache;

	public static void init(Context context) {
		File cacheDir = EnvironmentInfo.getDiskCacheDir(context, "http");
		long byteSize = 10 * 1024 * 1024L;
		HttpCacheLoder cacheLoder = null;
		try {
			cacheLoder = new HttpCacheLoder(cacheDir, byteSize);
		} catch (IOException e) {
			MyLog.e(e);
		}
		localCache = new LocalCache(cacheLoder);
	}

	public static <T> void load(String url, CacheHttpListener<T> httpHandler) {
		load(url, null, httpHandler, true);
	}

	public static <T> void load(String url, Header[] header,
			CacheHttpListener<T> httpHandler) {
		load(url, header, httpHandler, true);
	}

	public static <T> void load(final String url, final Header[] header,
			final CacheHttpListener<T> httpHandler, boolean cacheFlag) {
		// 如果有缓存，并且缓存未失效，则返回成功，否则http请求
		// 要确保url和参数都一致!!把post参数与url合并

		if (cacheFlag) {
			httpHandler.setCache(url);

			final HttpCacheEntry entry = getCache(url);

			if (entry != null) {
				try {
					String content = entry.getContent();
					if (!Utils.isEmpty(content)) {
						byte[] responseBytes = content.getBytes();
						Header[] headers = Utils.genHeader(entry.getHeaders());
						T responseMap = httpHandler.parseResponse(headers,
								responseBytes);
						if (null != responseMap) {
							httpHandler.onSuccess(200, entry.getHeaders(),
									responseMap);
							// log
							if (MyLog.isDebug()) {
								MyLog.d("header:----------------------------------------");
								Set<Entry<String, String>> entrySet = entry
										.getHeaders().entrySet();
								for (Entry<String, String> mapEntry : entrySet) {
									MyLog.d(mapEntry.getKey() + ":"
											+ mapEntry.getValue());
								}
								MyLog.d("responseBody:----------------------------------------");
								MyLog.d(String.valueOf(responseMap));
							}
							return;
						}
					}
				} catch (Throwable e) {
					MyLog.e(e);
				}
			}
		}

		HttpUtil.exec(url, header, httpHandler);
	}

	public static void saveCache(String key, HttpCacheEntry value) {
		localCache.put(key, value);
	}

	public static HttpCacheEntry getCache(String key) {
		return localCache.get(key);
	}
}
