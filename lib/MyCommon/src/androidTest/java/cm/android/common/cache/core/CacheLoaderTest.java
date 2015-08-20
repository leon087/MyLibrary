package cm.android.common.cache.core;

import android.content.Context;
import android.support.v4.util.LruCache;
import android.test.InstrumentationTestCase;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import cm.android.common.cache.disk.cache.HttpCache;
import cm.android.common.cache.disk.entry.HttpCacheEntry;
import cm.android.util.EnvironmentUtil;

public class CacheLoaderTest extends InstrumentationTestCase {

    public void testPutAndGet() throws Exception {
        Context context = getInstrumentation().getContext();

        File cacheDir = EnvironmentUtil.getCacheDir(context, "http");
        long byteSize = 10 * 1024 * 1024L;
        HttpCache cacheLoder = null;
        try {
            cacheLoder = new HttpCache(cacheDir, byteSize);
        } catch (IOException e) {
        }
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        LruCache memoryCache = new LruCache(cacheSize);
        CacheLoader localCache = new CacheLoader(memoryCache, cacheLoder);

        HttpCacheEntry entry = new HttpCacheEntry();
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("test", "test");
        entry.setHeaders(headers);
        String content = "testContent";
        entry.setContent(content);

        String uri = "http://test";
        entry.setUri(uri);

        localCache.put(uri, entry);
        HttpCacheEntry testEntry = localCache.get(uri);

        assertEquals("testContent", testEntry.getContent());
    }
}