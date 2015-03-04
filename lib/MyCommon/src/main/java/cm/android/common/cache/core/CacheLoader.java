package cm.android.common.cache.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.v4.util.LruCache;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cm.java.util.HexUtil;

public class CacheLoader {

    private static final Logger logger = LoggerFactory.getLogger("CACHE");

    private ICache cache;

    private LruCache memoryCache;

    public <V> CacheLoader(LruCache memoryCache, ICache<String, V> cache) {
        if (memoryCache == null || cache == null) {
            throw new NullPointerException("memoryCache = " + memoryCache + ",cache = " + cache);
        }
        this.cache = cache;
        this.memoryCache = memoryCache;
    }

    public void release() {
        memoryCache.evictAll();
    }

    public void clear() {
        cache.clear();
        memoryCache.evictAll();
    }

    public <V> V get(String key) {
        key = toKey(key);
        V value = (V) memoryCache.get(key);
        if (value != null) {
            return value;
        }

        if (cache.isExpire(key)) {
            cache.delete(key);
            return null;
        } else {
            value = (V) cache.get(key);
            logger.info("key = {},value = {}", key, value);
            return value;
        }
    }

    public <V> void put(String key, V value) {
        key = toKey(key);
        logger.info("key = {},value = {}", key, value);
        // 写入本地
        cache.put(key, value);
        memoryCache.put(key, value);
    }

    private String toKey(String uri) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5bytes = messageDigest.digest(uri.getBytes());
            return HexUtil.encode(md5bytes);
        } catch (NoSuchAlgorithmException e) {
            // throw new AssertionError(e);
            logger.error("uri = " + uri, e);
            return String.valueOf(uri.hashCode());
        }
    }
}
