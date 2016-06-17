package cm.android.common.cache.core;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.support.v4.util.LruCache;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import cm.android.common.cache.disk.cache.HttpCache;
import cm.android.common.cache.disk.entry.HttpCacheEntry;
import cm.android.util.BuildConfig;
import cm.android.util.EnvironmentUtil;

import static junit.framework.TestCase.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class CacheLoaderTest {

    @Test
    public void testPutAndGet() throws Exception {
        Context context = RuntimeEnvironment.application;

        File cacheDir = EnvironmentUtil.getCacheDir(context, "http");
        long byteSize = 10 * 1024 * 1024L;
        HttpCache cacheLoder = null;
        try {
            cacheLoder = new HttpCache(cacheDir, byteSize);
        } catch (IOException e) {
        }
//        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        Random random = new Random();
        int maxMemory = random.nextInt(1024) * 10 + 1024;
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