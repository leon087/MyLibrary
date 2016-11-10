package cm.java.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonUtil {

    private static final Logger logger = LoggerFactory.getLogger("util");

    private static final String DEF_VALUE_STRING = "";

    private static final int DEF_VALUE_INT = -1;

    private static final boolean DEF_VALUE_BOOLEAN = false;

    private JsonUtil() {
    }

    /**
     * 根据key获取value,不存在或异常返回{@value #DEF_VALUE_STRING}
     */
    public static String getString(JSONObject obj, String key) {
        return getString(obj, key, DEF_VALUE_STRING);
    }

    public static String getString(JSONObject obj, String key, String defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        try {
            return obj.getString(key);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            return defaultValue;
        }
    }

    /**
     * 根据key获取value,不存在或异常返回{@value #DEF_VALUE_BOOLEAN}
     */
    public static boolean getBoolean(JSONObject obj, String key) {
        return getBoolean(obj, key, DEF_VALUE_BOOLEAN);
    }

    public static boolean getBoolean(JSONObject obj, String key, boolean defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        try {
            return obj.getBoolean(key);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            return defaultValue;
        }
    }

    /**
     * 根据key获取value,不存在或异常返回{@value #DEF_VALUE_INT}
     */
    public static long getLong(JSONObject obj, String key) {
        return getLong(obj, key, DEF_VALUE_INT);
    }

    public static long getLong(JSONObject obj, String key, long defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        try {
            return obj.getLong(key);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            return defaultValue;
        }
    }

    /**
     * 根据key获取value,不存在或异常返回{@value #DEF_VALUE_INT}
     */
    public static int getInt(JSONObject obj, String key) {
        return getInt(obj, key, DEF_VALUE_INT);
    }

    public static int getInt(JSONObject obj, String key, int defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        try {
            return obj.getInt(key);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            return defaultValue;
        }
    }

    /**
     * 根据key获取value,不存在或异常返回{@value #DEF_VALUE_INT}
     */
    public static double getDouble(JSONObject obj, String key) {
        return getDouble(obj, key, DEF_VALUE_INT);
    }

    public static double getDouble(JSONObject obj, String key, double defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        try {
            return obj.getDouble(key);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            return defaultValue;
        }
    }

    /**
     * 根据key获取value,不存在或异常返回{@value #DEF_VALUE_INT}
     */
    public static float getFloat(JSONObject obj, String key) {
        return getFloat(obj, key, DEF_VALUE_INT);
    }

    public static float getFloat(JSONObject obj, String key, float defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        try {
            return Parse.getFloat(obj, key);
        } catch (ParseUtil.ParseException e) {
            logger.error(e.getMessage());
            return defaultValue;
        }
    }


    public static JSONArray getArray(JSONObject obj, String key) {
        if (obj == null) {
            return new JSONArray();
        }
        try {
            return obj.getJSONArray(key);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            return new JSONArray();
        }
    }

    public static JSONObject getJSONObject(JSONObject obj, String key) {
        if (obj == null) {
            return new JSONObject();
        }
        try {
            return obj.getJSONObject(key);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            return new JSONObject();
        }
    }

    public static Object getObject(JSONObject obj, String key, Object defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        try {
            return obj.get(key);
        } catch (JSONException e) {
            logger.error(e.getMessage());
            return defaultValue;
        }
    }

//    /**
//     * 根据key获取map中的List，如果不存在，返回空List(不返回null);只获取List类型的值，其他类型空List(不返回null)
//     */
//    public static <K, V, P> List<P> getList(JSONObject obj, String key) {
//        if (obj == null) {
//            return ObjectUtil.newArrayList();
//        }
//        try {
//            Object value = obj.get(key);
//            if (value instanceof List) {
//                return (List<P>) value;
//            } else {
//                return ObjectUtil.newArrayList();
//            }
//        } catch (JSONException e) {
//            return ObjectUtil.newArrayList();
//        }
//    }

//    public static <K, V, T, P> Map<T, P> getMap(JSONObject obj, String key) {
//        if (map == null) {
//            return ObjectUtil.newHashMap();
//        }
//        V value = map.get(key);
//        if (value instanceof Map) {
//            return (Map<T, P>) value;
//        } else {
//            return ObjectUtil.newHashMap();
//        }
//    }

    private static class Parse {
        public static Object get(JSONObject obj, String name) throws ParseUtil.ParseException {
            if (obj == null) {
                throw new ParseUtil.ParseException("map = null");
            }
            try {
                Object result = obj.get(name);
                return result;
            } catch (JSONException e) {
                throw new ParseUtil.ParseException(e.getMessage());
            }
        }

        public static float getFloat(JSONObject obj, String name) throws ParseUtil.ParseException {
            Object object = get(obj, name);
            Float result = ParseUtil.toFloat(object);
            if (result == null) {
                throw ParseUtil.typeMismatch(name, object, "float");
            }
            return result;
        }
    }
}
