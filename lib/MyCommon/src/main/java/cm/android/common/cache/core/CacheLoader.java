package cm.android.common.cache.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cm.android.util.ByteUtil;

public class CacheLoader {

    private static final Logger logger = LoggerFactory.getLogger("CACHE");

    private ICache cache;

    public <V> CacheLoader(ICache<String, V> cache) {
        if (cache == null) {
            throw new NullPointerException("cache = null");
        }
        this.cache = cache;
    }

    public void release() {
        cache = null;
    }

    public void clear() {
        cache.clear();
    }

    public <V> V get(String key) {
        key = toKey(key);
        if (cache.isExpire(key)) {
            cache.delete(key);
            return null;
        } else {
            V value = (V) cache.get(key);
            logger.info("key = {},value = {}", key, value);
            return value;
        }
    }

    public <V> void put(String key, V value) {
        key = toKey(key);
        logger.info("key = {},value = {}", key, value);
        // 写入本地
        cache.put(key, value);
    }

    private String toKey(String uri) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5bytes = messageDigest.digest(uri.getBytes());
            return ByteUtil.bytesToHexString(md5bytes, false);
        } catch (NoSuchAlgorithmException e) {
            // throw new AssertionError(e);
            logger.error("uri = " + uri, e);
            return String.valueOf(uri.hashCode());
        }
    }
}
