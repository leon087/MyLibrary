package cm.android.common.cache.core;

import cm.android.util.ByteUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("unchecked")
public class LocalCache {
    private static final Logger logger = LoggerFactory.getLogger("CACHE");

    @SuppressWarnings("rawtypes")
    private ILocalCache localCache;

    public <V> LocalCache(ILocalCache<String, V> localCache) {
        this.localCache = localCache;
    }

    public void release() {
    }

    public void clear() {
        if (null == localCache) {
            return;
        }

        localCache.clear();
    }

    public <V> V get(String key) {
        if (null == localCache) {
            return null;
        }

        key = toKey(key);
        if (localCache.isExpire(key)) {
            localCache.delete(key);
            return null;
        } else {
            V value = (V) localCache.get(key);
            logger.info("key = {},value = {}", key, value);
            return value;
        }
    }

    public <V> void put(String key, V value) {
        if (null == localCache) {
            return;
        }

        key = toKey(key);
        logger.info("key = {},value = {}", key, value);
        // 写入本地
        localCache.put(key, value);
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
