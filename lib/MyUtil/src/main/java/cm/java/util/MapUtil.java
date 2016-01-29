package cm.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

public class MapUtil {

    private static final Logger logger = LoggerFactory.getLogger("util");

    private static final String DEF_VALUE_STRING = "";

    private static final int DEF_VALUE_INT = -1;

    private static final boolean DEF_VALUE_BOOLEAN = false;

    private MapUtil() {
    }

    /**
     * 根据key获取value,不存在或异常返回{@value #DEF_VALUE_STRING}
     */
    public static <K, V> String getString(Map<K, V> map, K key) {
        return getString(map, key, DEF_VALUE_STRING);
    }

    public static <K, V> String getString(Map<K, V> map, K key, String defaultValue) {
        try {
            return Parse.getString(map, key);
        } catch (ParseUtil.ParseException e) {
            logger.error(e.getMessage(), e);
            return defaultValue;
        }
    }

    /**
     * 根据key获取value,不存在或异常返回{@value #DEF_VALUE_BOOLEAN}
     */
    public static <K, V> boolean getBoolean(Map<K, V> map, K key) {
        return getBoolean(map, key, DEF_VALUE_BOOLEAN);
    }

    public static <K, V> boolean getBoolean(Map<K, V> map, K key, boolean defaultValue) {
        try {
            return Parse.getBoolean(map, key);
        } catch (ParseUtil.ParseException e) {
            logger.error(e.getMessage(), e);
            return defaultValue;
        }
    }

    /**
     * 根据key获取value,不存在或异常返回{@value #DEF_VALUE_INT}
     */
    public static <K, V> long getLong(Map<K, V> map, K key) {
        return getLong(map, key, DEF_VALUE_INT);
    }

    public static <K, V> long getLong(Map<K, V> map, K key, long defaultValue) {
        try {
            return Parse.getLong(map, key);
        } catch (ParseUtil.ParseException e) {
            logger.error(e.getMessage(), e);
            return defaultValue;
        }
    }

    /**
     * 根据key获取value,不存在或异常返回{@value #DEF_VALUE_INT}
     */
    public static <K, V> int getInt(Map<K, V> map, K key) {
        return getInt(map, key, DEF_VALUE_INT);
    }

    public static <K, V> int getInt(Map<K, V> map, K key, int defaultValue) {
        try {
            return Parse.getInt(map, key);
        } catch (ParseUtil.ParseException e) {
            logger.error(e.getMessage(), e);
            return defaultValue;
        }
    }

    /**
     * 根据key获取value,不存在或异常返回{@value #DEF_VALUE_INT}
     */
    public static <K, V> double getDouble(Map<K, V> map, K key) {
        return getDouble(map, key, DEF_VALUE_INT);
    }

    public static <K, V> double getDouble(Map<K, V> map, K key, double defaultValue) {
        try {
            return Parse.getDouble(map, key);
        } catch (ParseUtil.ParseException e) {
            logger.error(e.getMessage(), e);
            return defaultValue;
        }
    }

    /**
     * 根据key获取value,不存在或异常返回{@value #DEF_VALUE_INT}
     */
    public static <K, V> float getFloat(Map<K, V> map, K key) {
        return getFloat(map, key, DEF_VALUE_INT);
    }

    public static <K, V> float getFloat(Map<K, V> map, K key, float defaultValue) {
        try {
            return Parse.getFloat(map, key);
        } catch (ParseUtil.ParseException e) {
            logger.error(e.getMessage(), e);
            return defaultValue;
        }
    }

    /**
     * 根据key获取value，如果不存在，返回空List(不返回null);只获取List类型的值，其他类型空List(不返回null)
     */
    public static <K, V, P> List<P> getList(Map<K, V> map, K key) {
        try {
            return Parse.getList(map, key);
        } catch (ParseUtil.ParseException e) {
            logger.error(e.getMessage(), e);
            return ObjectUtil.newArrayList(0);
        }
    }

    public static <K, V, T, P> Map<T, P> getMap(Map<K, V> map, K key) {
        try {
            return Parse.getMap(map, key);
        } catch (ParseUtil.ParseException e) {
            logger.error(e.getMessage(), e);
            return ObjectUtil.newHashMap(0);
        }
    }

    private static class Parse {
        public static <K, V> Object get(Map<K, V> map, K name) throws ParseUtil.ParseException {
            if (map == null) {
                throw new ParseUtil.ParseException("map = null");
            }
            Object result = map.get(name);
            if (result == null) {
                throw new ParseUtil.ParseException("No value for " + name);
            }
            return result;
        }

        public static <K, V> boolean getBoolean(Map<K, V> map, K name) throws ParseUtil.ParseException {
            Object object = get(map, name);
            Boolean result = ParseUtil.toBoolean(object);
            if (result == null) {
                throw ParseUtil.typeMismatch(name, object, "boolean");
            }
            return result;
        }

        public static <K, V> float getFloat(Map<K, V> map, K name) throws ParseUtil.ParseException {
            Object object = get(map, name);
            Float result = ParseUtil.toFloat(object);
            if (result == null) {
                throw ParseUtil.typeMismatch(name, object, "float");
            }
            return result;
        }

        public static <K, V> double getDouble(Map<K, V> map, K name) throws ParseUtil.ParseException {
            Object object = get(map, name);
            Double result = ParseUtil.toDouble(object);
            if (result == null) {
                throw ParseUtil.typeMismatch(name, object, "double");
            }
            return result;
        }

        public static <K, V> long getLong(Map<K, V> map, K name) throws ParseUtil.ParseException {
            Object object = get(map, name);
            Long result = ParseUtil.toLong(object);
            if (result == null) {
                throw ParseUtil.typeMismatch(name, object, "long");
            }
            return result;
        }

        public static <K, V> int getInt(Map<K, V> map, K name) throws ParseUtil.ParseException {
            Object object = get(map, name);
            Integer result = ParseUtil.toInteger(object);
            if (result == null) {
                throw ParseUtil.typeMismatch(name, object, "int");
            }
            return result;
        }

        public static <K, V> String getString(Map<K, V> map, K name) throws ParseUtil.ParseException {
            Object object = get(map, name);
            String result = ParseUtil.toString(object);
            if (result == null) {
                throw ParseUtil.typeMismatch(name, object, "String");
            }
            return result;
        }

        public static <K, V, P> List<P> getList(Map<K, V> map, K name) throws ParseUtil.ParseException {
            Object object = get(map, name);
            if (object instanceof List) {
                return (List<P>) object;
            } else {
                throw ParseUtil.typeMismatch(name, object, "List");
            }
        }

        public static <K, V, T, P> Map<T, P> getMap(Map<K, V> map, K name) throws ParseUtil.ParseException {
            Object object = get(map, name);
            if (object instanceof Map) {
                return (Map<T, P>) object;
            } else {
                throw ParseUtil.typeMismatch(name, object, "Map");
            }
        }
    }
}
