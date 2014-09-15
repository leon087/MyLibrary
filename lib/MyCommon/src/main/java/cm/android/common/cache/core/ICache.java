package cm.android.common.cache.core;

public interface ICache<K, V> {
    void put(K key, V value);

    V get(K key);

    void delete(K key);

    void clear();

    boolean isExpire(K key);

    long size();

    long getMaxSize();
}
