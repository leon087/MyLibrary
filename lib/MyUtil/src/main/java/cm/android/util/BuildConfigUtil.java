package cm.android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.support.v4.util.ArrayMap;

import java.lang.reflect.Field;
import java.util.Map;

import cm.java.util.MapUtil;

public final class BuildConfigUtil {
    private static final Logger logger = LoggerFactory.getLogger("BuildConfigs");
    public static final Map<String, Object> pool = new ArrayMap<>();
    private static final String TAG = "null";

    /**
     * 需要保证buildConfig未被混淆
     */
    public static final void config(String tag, Class buildConfig) {
        if (!buildConfig.getSimpleName().contains("BuildConfig")) {
            throw new RuntimeException("buildConfig = " + buildConfig.getName());
        }

        Field[] fields = buildConfig.getDeclaredFields();
        for (Field field : fields) {
            try {
                String key = field.getName();
                field.setAccessible(true);
                put(tag, key, field.get(key));
            } catch (IllegalAccessException e) {
                logger.error("field = " + field, e);
            }
        }
    }

    private static final String getKey(String tag, String key) {
        return tag + key;
    }

    public static final void put(String tag, String key, Object value) {
        pool.put(getKey(tag, key), value);
    }

    public static final boolean get(String tag, String key, boolean defValue) {
        return MapUtil.getBoolean(pool, getKey(tag, key), defValue);
    }

    public static final int get(String tag, String key, int defValue) {
        return MapUtil.getInt(pool, getKey(tag, key), defValue);
    }

    public static final String get(String tag, String key, String defValue) {
        return MapUtil.getString(pool, getKey(tag, key), defValue);
    }

    public static final void config(Class buildConfig) {
        config(TAG, buildConfig);
    }

    public static final void put(String key, Object value) {
        put(TAG, key, value);
    }

    public static final boolean get(String key, boolean defValue) {
        return get(TAG, key, defValue);
    }

    public static final int get(String key, int defValue) {
        return get(TAG, key, defValue);
    }

    public static final String get(String key, String defValue) {
        return get(TAG, key, defValue);
    }
}
