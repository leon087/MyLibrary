package wd.android.common.cache.disk.local;

import java.io.File;
import java.io.IOException;

import wd.android.common.cache.core.ILocalCache;
import wd.android.util.util.IoUtil;
import wd.android.util.util.MyLog;

import com.jakewharton.disklrucache.DiskLruCache;

public abstract class DiskCacheLoder<V> implements ILocalCache<String, V> {
	// HttpResponseCache的使用 缓存 cache,Caches HTTP and HTTPS responses to the
	// filesystem so they may be reused, saving time and bandwidth. This class
	// supports HttpURLConnection and HttpsURLConnection; there is no
	// platform-provided cache for DefaultHttpClient or AndroidHttpClient.

	private static final int VERSION = 201308;
	private static final int ENTRY_COUNT = 1;
	protected final DiskLruCache cache;

	public DiskCacheLoder(File directory, long maxSize) throws IOException {
		cache = DiskLruCache.open(directory, VERSION, ENTRY_COUNT, maxSize);
	}

	public void release() {
		try {
			cache.flush();
			cache.close();
		} catch (IOException e) {
			MyLog.e(e);
		}
		IoUtil.closeQuietly(cache);
	}

	@Override
	public void put(String key, V value) {
		DiskLruCache.Editor editor = null;
		try {
			editor = cache.edit(key);
			if (editor == null) {
				return;
			}
			writeTo(value, editor);
			editor.commit();
		} catch (IOException e) {
			abortQuietly(editor);
		}
	}

	private void abortQuietly(DiskLruCache.Editor editor) {
		// Give up because the cache cannot be written.
		try {
			if (editor != null) {
				editor.abort();
			}
		} catch (IOException ignored) {
			MyLog.e(ignored);
		}
	}

	@Override
	public void delete(String key) {
		try {
			cache.remove(key);
		} catch (IOException e) {
			MyLog.e(e);
		}
	}

	@Override
	public V get(String key) {
		DiskLruCache.Snapshot snapshot;
		try {
			snapshot = cache.get(key);

			if (snapshot == null) {
				return null;
			}
			return readFrom(snapshot);
		} catch (IOException e) {
			// Give up because the cache cannot be read.
			MyLog.e(e);
			return null;
		}
	}

	@Override
	public void clear() {
		try {
			cache.delete();
		} catch (IOException e) {
			MyLog.e(e);
		}
	}

	public abstract void writeTo(V value, DiskLruCache.Editor editor)
			throws IOException;

	public abstract V readFrom(DiskLruCache.Snapshot snapshot)
			throws IOException;
}
