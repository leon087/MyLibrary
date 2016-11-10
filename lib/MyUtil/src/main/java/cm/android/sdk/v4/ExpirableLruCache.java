package cm.android.sdk.v4;

import android.os.SystemClock;
import android.support.v4.util.ArrayMap;

import java.util.Map;
import java.util.concurrent.TimeUnit;

public class ExpirableLruCache<K, V> {
    public static final class Builder<K, V> {
        private long expire;
        private int maxSize;

        public Builder() {
            expire = TimeUnit.HOURS.toMillis(6);
            maxSize = 100;
        }

        public Builder<K, V> expire(long duration, TimeUnit unit) {
            if (duration < 0) {
                throw new IllegalArgumentException("duration < 0");
            }
            if (unit == null) {
                throw new NullPointerException("unit == null");
            }

            this.expire = unit.toMillis(duration);
            return this;
        }

        public Builder<K, V> maxSize(int maxSize) {
            if (maxSize < 0) {
                throw new IllegalArgumentException("maxSize < 0");
            }
            this.maxSize = maxSize;
            return this;
        }

        public ExpirableLruCache<K, V> build() {
            return new ExpirableLruCache<>(this);
        }
    }

    private static final class Expire<K> {
        private final Map<K, Long> timestamp = new ArrayMap<>();
        private long expire;

        void put(K key) {
            timestamp.put(key, elapsedRealtime() + expire);
        }

        void setExpire(long expire) {
            this.expire = expire;
        }

        private long elapsedRealtime() {
            return SystemClock.elapsedRealtime();
        }

        boolean isExpired(K key) {
            Long t = timestamp.get(key);
            if (t != null && elapsedRealtime() >= t) {
                return true;
            }
            return false;
        }

        void remove(K key) {
            timestamp.remove(key);
        }
    }

    private android.support.v4.util.LruCache<K, V> cache;
    private Expire<K> expire = new Expire<>();

    public ExpirableLruCache(Builder builder) {
        cache = new android.support.v4.util.LruCache<K, V>(builder.maxSize) {
            @Override
            public int sizeOf(K key, V value) {
                return super.sizeOf(key, value);
            }

            @Override
            protected void entryRemoved(boolean evicted, K key, V oldValue, V newValue) {
                super.entryRemoved(evicted, key, oldValue, newValue);
                expire.remove(key);
            }
        };
        expire.setExpire(builder.expire);
    }

    public void put(K key, V value) {
        expire.put(key);
        cache.put(key, value);
    }

    public V get(K key) {
        if (isExpired(key)) {
            remove(key);
            return null;
        }
        return cache.get(key);
    }

    public void remove(K key) {
        cache.remove(key);
    }

    public void evictAll() {
        cache.evictAll();
    }

    public boolean isExpired(K key) {
        return expire.isExpired(key);
    }

    public long size() {
        return cache.size();
    }

    public long maxSize() {
        return cache.maxSize();
    }
}
