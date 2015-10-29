package cm.android.common.http;

import java.util.Map;

import cm.android.common.cache.disk.entry.HttpCacheEntry;
import cz.msebera.android.httpclient.Header;

public abstract class CacheHttpListener<T> extends MyJsonHttpListener<T> {

    private CacheHolder cacheHolder = new CacheHolder();

    /**
     *
     */
    public CacheHttpListener() {
        super();
    }

    public void setCache(String url) {
        cacheHolder.setCache(url);
    }

    @Override
    final void onSuccess(int statusCode, Header[] headers,
            byte[] responseBytes, T responseMap) {
        // Map<String, Object> responseMap = JSON.parseObject(
        // responseBody.getBytes(), Map.class);

        Map<String, String> headMap = HttpUtil.genHeaderMap(headers);

        // 保存缓存
        cacheHolder.saveCache(headMap, responseBytes);
        // 返回数据
        onSuccess(statusCode, headMap, responseBytes, responseMap);
    }

    private static class CacheHolder {

        private HttpCacheEntry entry = null;

        private void saveCache(Map<String, String> headers, byte[] responseBytes) {
            if (entry != null) {
                entry.setHeaders(headers);
                String content = new String(responseBytes);
                entry.setContent(content);
                HttpLoader.saveCache(entry.getUri(), entry);
            }
        }

        public void setCache(String url) {
            entry = new HttpCacheEntry();
            entry.setUri(url);
        }
    }
}
