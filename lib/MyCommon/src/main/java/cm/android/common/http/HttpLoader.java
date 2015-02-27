package cm.android.common.http;

import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.support.v4.util.LruCache;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;

import cm.android.common.cache.core.CacheLoader;
import cm.android.common.cache.disk.cache.HttpCache;
import cm.android.common.cache.disk.entry.HttpCacheEntry;
import cm.android.util.EnvironmentUtil;
import cm.java.util.Utils;

/**
 * 带有缓存的数据请求接口
 */
public class HttpLoader {

    private static final Logger logger = LoggerFactory.getLogger("HTTP");

    // 后续会替换掉
    // public static GuavaCache<HttpCacheEntry> guavaCache;
    public static CacheLoader localCache;

    public static void init(Context context) {
        File cacheDir = EnvironmentUtil.getDiskCacheDir(context, "http");
        long byteSize = 10 * 1024 * 1024L;
        HttpCache cacheLoder = null;
        try {
            cacheLoder = new HttpCache(cacheDir, byteSize);
        } catch (IOException e) {
            logger.error("", e);
        }
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        LruCache memoryCache = new LruCache(cacheSize);
        localCache = new CacheLoader(memoryCache, cacheLoder);
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
                            httpHandler.onSuccess(200, entry.getHeaders(), responseBytes,
                                    responseMap);
                            // log
                            if (logger.isDebugEnabled()) {
                                logger.debug("header:----------------------------------------");
                                Set<Entry<String, String>> entrySet = entry
                                        .getHeaders().entrySet();
                                for (Entry<String, String> mapEntry : entrySet) {
                                    logger.debug(mapEntry.getKey() + ":"
                                            + mapEntry.getValue());
                                }
                                logger.debug(
                                        "responseBody:----------------------------------------");
                                logger.debug(String.valueOf(responseMap));
                            }
                            return;
                        }
                    }
                } catch (Throwable e) {
                    logger.error("", e);
                }
            }
        }

        HttpUtil.exec(null, url, header, (RequestParams) null, httpHandler);
    }

    public static void saveCache(String key, HttpCacheEntry value) {
        localCache.put(key, value);
    }

    public static HttpCacheEntry getCache(String key) {
        return localCache.get(key);
    }
}
