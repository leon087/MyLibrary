package cm.android.sdk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.v4.util.LruCache;

import java.lang.ref.SoftReference;
import java.util.LinkedHashMap;

/**
 * 内存缓存
 */
public abstract class MemoryCache<K, V> {

    private static final Logger logger = LoggerFactory.getLogger("MemoryCache");

    /**
     * 从内存读取数据速度是最快的，为了更大限度使用内存，这里使用了两层缓存。 硬引用缓存不会轻易被回收，用来保存常用数据，不常用的转入软引用缓存。
     */
    private static final int SOFT_CACHE_SIZE = 15; // 软引用缓存容量

    private LruCache<K, V> mLruCache; // 硬引用缓存

    private LinkedHashMap<K, SoftReference<V>> mSoftCache; // 软引用缓存

    private float defaultPercent = 0.5F;

    public MemoryCache(float cacheSizePercent) {
        // int memClass = ((ActivityManager) context
        // .getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        // int cacheSize = 1024 * 1024 * memClass / 16; // 硬引用缓存容量，为系统可用内存的1/4
        this.defaultPercent = cacheSizePercent;
        int cacheSize = getMemCacheSizePercent(defaultPercent);
        mLruCache = new LruCache<K, V>(cacheSize) {
            @Override
            protected int sizeOf(K key, V value) {
                final int size = getSize(value) / 1024;
                return size == 0 ? 1 : size;
            }

            @Override
            protected void entryRemoved(boolean evicted, K key, V oldValue,
                    V newValue) {
                if (oldValue != null) {
                    // 硬引用缓存容量满的时候，会根据LRU算法把最近没有被使用的对象转入此软引用缓存
                    mSoftCache.put(key, new SoftReference<V>(oldValue));
                }
            }
        };
        mSoftCache = new LinkedHashMap<K, SoftReference<V>>(SOFT_CACHE_SIZE,
                0.75f, true) {
            private static final long serialVersionUID = 6040103833179403725L;

            @Override
            protected boolean removeEldestEntry(
                    Entry<K, SoftReference<V>> eldest) {
                if (size() > SOFT_CACHE_SIZE) {
                    return true;
                }
                return false;
            }
        };
    }

    /**
     * Sets the memory cache size based on a percentage of the max available VM
     * memory. Eg. setting percent to 0.2 would set the memory cache to one
     * fifth of the available memory. Throws {@link IllegalArgumentException} if
     * percent is < 0.05 or > .8. memCacheSize is stored in kilobytes instead of
     * bytes as this will eventually be passed to construct a LruCache which
     * takes an int in its constructor.
     * <p/>
     * This value should be chosen carefully based on a number of factors Refer
     * to the corresponding Android Training class for more discussion:
     * http://developer.android.com/training/displaying-bitmaps/
     *
     * @param percent Percent of available app memory to use to size memory cache
     */
    int getMemCacheSizePercent(float percent) {
        if (percent < 0.05f || percent > 0.8f) {
            throw new IllegalArgumentException(
                    "setMemCacheSizePercent - percent must be "
                            + "between 0.05 and 0.8 (inclusive)");
        }
        return Math.round(percent * Runtime.getRuntime().maxMemory() / 1024);
    }

    /**
     * Get the size in bytes.
     *
     * @return size in bytes
     */
    protected abstract int getSize(V value);

    /**
     * 从缓存中获取
     */
    public V get(K key) {
        V value;
        // 先从硬引用缓存中获取
        synchronized (mLruCache) {
            value = mLruCache.get(key);
            if (value != null) {
                // 如果找到的话，把元素移到LinkedHashMap的最前面，从而保证在LRU算法中是最后被删除
                // mLruCache.remove(key);
                // mLruCache.put(key, value);
                return value;
            }
        }
        // 如果硬引用缓存中找不到，到软引用缓存中找
        synchronized (mSoftCache) {
            SoftReference<V> valueReference = mSoftCache.remove(key);
            if (valueReference != null) {
                value = valueReference.get();
                if (value != null) {
                    mLruCache.put(key, value);
                }
            }
        }
        return value;
    }

    /**
     * 添加到缓存
     */
    public void put(K key, V value) {
        if (value != null) {
            synchronized (mLruCache) {
                mSoftCache.remove(key);
                mLruCache.put(key, value);
            }
        }
    }

    public void clearCache() {
        logger.info("mLruCache = {},mSoftCache = {}", mLruCache.size(), mSoftCache.size());
        synchronized (mLruCache) {
            mLruCache.evictAll();
        }
        mSoftCache.clear();
    }

    /**
     * 释放资源
     */
    public void release() {
        clearCache();
    }
}
