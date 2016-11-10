package cm.android.common.cache.memory;

import cm.android.common.cache.core.ICache;
import cm.android.sdk.v4.ExpirableLruCache;

public class MemoryCache<V> implements ICache<String, V> {

    //    private android.support.v4.util.LruCache<String, V> cache;
    private ExpirableLruCache<String, V> cache;

    public MemoryCache(int maxSize) {
        cache = new ExpirableLruCache.Builder<String, V>()
                .maxSize(maxSize)
                .build();
    }

    @Override
    public void put(String key, V value) {
        cache.put(key, value);
    }

    @Override
    public V get(String key) {
        return cache.get(key);
    }

    @Override
    public void delete(String key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.evictAll();
    }

    @Override
    public boolean isExpire(String key) {
        return false;
    }

    @Override
    public long size() {
        return cache.size();
    }

    @Override
    public long getMaxSize() {
        return cache.maxSize();
    }
}
