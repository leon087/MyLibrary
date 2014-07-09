package cm.android.common.cache.core;

public interface ILocalCache<K, V> {
	void put(K key, V value);

	V get(K key);

	void delete(K key);

	void clear();

	boolean isExpire(K key);

}
