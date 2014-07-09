package cm.android.common.cache.disk.local;

import cm.android.common.cache.disk.entry.HttpCacheEntry;
import com.jakewharton.disklrucache.DiskLruCache.Editor;
import com.jakewharton.disklrucache.DiskLruCache.Snapshot;

import java.io.File;
import java.io.IOException;

public class HttpCacheLoder extends DiskCacheLoder<HttpCacheEntry> {
	// HttpResponseCache的使用 缓存 cache,Caches HTTP and HTTPS responses to the
	// filesystem so they may be reused, saving time and bandwidth. This class
	// supports HttpURLConnection and HttpsURLConnection; there is no
	// platform-provided cache for DefaultHttpClient or AndroidHttpClient.

	public HttpCacheLoder(File directory, long maxSize) throws IOException {
		super(directory, maxSize);
	}

	@Override
	public void writeTo(HttpCacheEntry value, Editor editor) throws IOException {
		value.writeTo(editor);
	}

	@Override
	public HttpCacheEntry readFrom(Snapshot snapshot) throws IOException {
		HttpCacheEntry entry = new HttpCacheEntry();
		entry.readFrom(snapshot);
		return entry;
	}

	@Override
	public boolean isExpire(String key) {
		HttpCacheEntry entry = get(key);
		if (entry != null) {
			long time = Long.parseLong(entry.getTime());
			if (System.currentTimeMillis() - time < 24 * 60 * 60 * 1000) {
				return false;
			}
		}
		return true;
	}

}
