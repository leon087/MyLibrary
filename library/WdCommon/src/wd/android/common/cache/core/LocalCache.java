package wd.android.common.cache.core;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import wd.android.util.util.ByteUtil;
import wd.android.util.util.MyLog;

@SuppressWarnings("unchecked")
public class LocalCache {
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
			MyLog.i("key = " + key + ",value = " + value);
			return value;
		}
	}

	public <V> void put(String key, V value) {
		if (null == localCache) {
			return;
		}

		key = toKey(key);
		MyLog.i("key = " + key + ",value = " + value);
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
			MyLog.e(e);
			return String.valueOf(uri.hashCode());
		}
	}
}
