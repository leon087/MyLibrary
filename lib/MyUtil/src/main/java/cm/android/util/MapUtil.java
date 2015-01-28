package cm.android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * JSON数据处理Util类
 */
public class MapUtil {

    private static final Logger logger = LoggerFactory.getLogger(MapUtil.class);

    private static final String DEF_VALUE_STRING = "";

    private static final int DEF_VALUE_INT = -1;

    private static final boolean DEF_VALUE_BOOLEAN = false;

    private MapUtil() {
    }

    /**
     * 根据key获取map中的value,不存在或异常返回{@value #DEF_VALUE_STRING}
     */
    public static <K, V> String getString(Map<K, V> map, K key) {
        return getString(map, key, DEF_VALUE_STRING);
    }

    public static <K, V> String getString(Map<K, V> map, K key, String defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        V value = map.get(key);
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof String) {
            return (String) value;
        } else {
            try {
                return String.valueOf(value);
            } catch (Exception e) {
                logger.error("value = " + value, e);
                return defaultValue;
            }
        }
    }

    /**
     * 根据key获取map中的value,不存在或异常返回{@value #DEF_VALUE_BOOLEAN}
     */
    public static <K, V> boolean getBoolean(Map<K, V> map, K key) {
        return getBoolean(map, key, DEF_VALUE_BOOLEAN);
    }

    public static <K, V> boolean getBoolean(Map<K, V> map, K key, boolean defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        V value = map.get(key);
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Boolean) {
            // 数据类型匹配直接强转
            return (Boolean) value;
        } else {
            // 数据类型不匹配则格式化，异常返回默认值
            try {
                return Boolean.valueOf(value.toString());
            } catch (NumberFormatException e) {
                logger.error("value = " + value, e);
                return defaultValue;
            }
        }
    }

    /**
     * 根据key获取map中的value,不存在或异常返回{@value #DEF_VALUE_INT}
     */
    public static <K, V> long getLong(Map<K, V> map, K key) {
        return getLong(map, key, DEF_VALUE_INT);
    }

    public static <K, V> long getLong(Map<K, V> map, K key, long defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        V value = map.get(key);
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Long) {
            return (Long) value;
        } else {
            try {
                return Long.valueOf(value.toString());
            } catch (NumberFormatException e) {
                logger.error("value = " + value, e);
                return defaultValue;
            }
        }
    }

    /**
     * 根据key获取map中的value,不存在或异常返回{@value #DEF_VALUE_INT}
     */
    public static <K, V> int getInt(Map<K, V> map, K key) {
        return getInt(map, key, DEF_VALUE_INT);
    }

    public static <K, V> int getInt(Map<K, V> map, K key, int defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        V value = map.get(key);
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Integer) {
            return (Integer) value;
        } else {
            try {
                return Integer.valueOf(value.toString());
            } catch (NumberFormatException e) {
                logger.error("value = " + value, e);
                return defaultValue;
            }
        }
    }

    /**
     * 根据key获取map中的value,不存在或异常返回{@value #DEF_VALUE_INT}
     */
    public static <K, V> double getDouble(Map<K, V> map, K key) {
        return getDouble(map, key, DEF_VALUE_INT);
    }

    public static <K, V> double getDouble(Map<K, V> map, K key, double defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        V value = map.get(key);
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Double) {
            return (Double) value;
        } else {
            try {
                return Double.valueOf(value.toString());
            } catch (NumberFormatException e) {
                logger.error("value = " + value, e);
                return defaultValue;
            }
        }
    }

    /**
     * 根据key获取map中的value,不存在或异常返回{@value #DEF_VALUE_INT}
     */
    public static <K, V> float getFloat(Map<K, V> map, K key) {
        return getFloat(map, key, DEF_VALUE_INT);
    }

    public static <K, V> float getFloat(Map<K, V> map, K key, float defaultValue) {
        if (map == null) {
            return defaultValue;
        }
        V value = map.get(key);
        if (value == null) {
            return defaultValue;
        }

        if (value instanceof Float) {
            return (Float) value;
        } else {
            try {
                return Float.valueOf(value.toString());
            } catch (NumberFormatException e) {
                logger.error("value = " + value, e);
                return defaultValue;
            }
        }
    }

    /**
     * 根据key获取map中的List，如果不存在，返回空List(不返回null);只获取List类型的值，其他类型空List(不返回null)
     */
    public static <K, V, P> List<P> getList(Map<K, V> map, K key) {
        if (map == null) {
            return ObjectUtil.newArrayList();
        }
        V value = map.get(key);
        if (value instanceof List) {
            return (List<P>) value;
        } else {
            return ObjectUtil.newArrayList();
        }
    }

    /**
     * @param <K>
     * @param <V>
     * @param <T>
     * @param map
     * @param key
     * @return
     */
    public static <K, V, T, P> Map<T, P> getMap(Map<K, V> map, K key) {
        if (map == null) {
            return ObjectUtil.newHashMap();
        }
        V value = map.get(key);
        if (value instanceof Map) {
            return (Map<T, P>) value;
        } else {
            return ObjectUtil.newHashMap();
        }
    }
}
