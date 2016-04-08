package cm.android.common.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SqlUtil {

    private static final Logger logger = LoggerFactory.getLogger("db");

    @SuppressWarnings("rawtypes")
    private static Map<Class, Class> basicMap = new HashMap<Class, Class>();

    static {
        basicMap.put(int.class, Integer.class);
        basicMap.put(long.class, Long.class);
        basicMap.put(float.class, Float.class);
        basicMap.put(double.class, Double.class);
        basicMap.put(boolean.class, Boolean.class);
        basicMap.put(byte.class, Byte.class);
        basicMap.put(short.class, Short.class);
    }

    /**
     * 根据实体类 获得 实体类对应的表名
     */
    public static String getTableName(Class<?> clazz) {
        Table table = clazz.getAnnotation(Table.class);
        if (table == null || table.name().trim().length() == 0) {
            // 当没有注解的时候默认用类的名称作为表名,并把点（.）替换为下划线(_)
            // return clazz.getName().replace('.', '_');
            return clazz.getSimpleName();
        }
        return table.name();
    }

    /**
     * 将实体类转换成ContentValues
     */
    public static ContentValues getContentValues(Object bean) {
        ContentValues contentValues = new ContentValues();
        Field[] fields = bean.getClass().getDeclaredFields();
        for (Field field : fields) {
            // String filedType = field.getGenericType().toString();
            // if (String.class.toString().equals(object)) {
            // }
            field.setAccessible(true);
            try {
                Object object = field.get(bean);
                if (object instanceof String) {
                    contentValues.put(field.getName(), (String) object);
                } else if (object instanceof Byte) {
                    contentValues.put(field.getName(), (Byte) object);
                } else if (object instanceof Short) {
                    contentValues.put(field.getName(), (Short) object);
                } else if (object instanceof Integer) {
                    contentValues.put(field.getName(), (Integer) object);
                } else if (object instanceof Long) {
                    contentValues.put(field.getName(), (Long) object);
                } else if (object instanceof Float) {
                    contentValues.put(field.getName(), (Float) object);
                } else if (object instanceof Double) {
                    contentValues.put(field.getName(), (Double) object);
                } else if (object instanceof Boolean) {
                    contentValues.put(field.getName(), (Boolean) object);
                } else if (object instanceof byte[]) {
                    contentValues.put(field.getName(), (byte[]) object);
                }
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
        return contentValues;
    }

    /**
     * 通过Cursor转换成对应的VO。注意：Cursor里的字段名（可用别名）必须要和VO的属性名一致
     */
    public static <T> T parse(Cursor cursor, Class<T> clazz) {
        if (cursor == null || cursor.getCount() == 0) {
            return null;
        }
        T obj;
        try {
            cursor.moveToFirst();
            obj = setValues2Fields(cursor, clazz);
            return obj;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 通过Cursor转换成对应的VO集合。注意：Cursor里的字段名（可用别名）必须要和VO的属性名一致
     */
    public static <T> List<T> parseList(Cursor c, Class<T> clazz) {
        if (c == null) {
            return null;
        }
        List<T> list = new ArrayList<>();
        T obj;
        try {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                obj = setValues2Fields(c, clazz);
                list.add(obj);
                c.moveToNext();
            }
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 把值设置进类属性里
     */
    @SuppressWarnings("rawtypes")
    private static <T> T setValues2Fields(Cursor c, Class clazz) throws Exception {
        String[] columnNames = c.getColumnNames();// 字段数组

        Set<String> columnNameSets = new HashSet<String>();
        Collections.addAll(columnNameSets, columnNames);

        Constructor constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        T obj = (T) constructor.newInstance();
        Field[] fields = clazz.getDeclaredFields();

        for (Field _field : fields) {
            Class<? extends Object> typeClass = _field.getType();// 属性类型
            if (!columnNameSets.contains(_field.getName())) {
                break;
            }
            typeClass = getBasicClass(typeClass);
            if (isBasicType(typeClass)) {
                String _str = c.getString(c.getColumnIndex(_field.getName()));
//                if (_str == null) {
//                    break;
//                }
                _str = (_str == null) ? "" : _str;

                Constructor<? extends Object> cons = typeClass
                        .getConstructor(String.class);
                Object attribute = cons.newInstance(_str);
                _field.setAccessible(true);
                _field.set(obj, attribute);
            } else if (isArrayType(typeClass)) {
                byte[] attribute = c
                        .getBlob(c.getColumnIndex(_field.getName()));
                if (attribute == null) {
                    break;
                }
                _field.setAccessible(true);
                _field.set(obj, attribute);
            } else {
                Object obj2 = setValues2Fields(c, typeClass);// 递归
                _field.set(obj, obj2);
                break;
            }

        }
        return obj;
    }

    /**
     * 判断是不是基本类型
     */
    @SuppressWarnings("rawtypes")
    private static boolean isBasicType(Class typeClass) {
        if (typeClass.equals(Integer.class)
                || typeClass.equals(Long.class)
                || typeClass.equals(Float.class)
                || typeClass.equals(Double.class)
                || typeClass.equals(Boolean.class)
                || typeClass.equals(Byte.class)
                || typeClass.equals(Short.class)
                || typeClass.equals(String.class)) {
            return true;

        } else {
            return false;
        }
    }

    /**
     * 判断是否是byte[]
     */
    private static boolean isArrayType(Class<?> typeClass) {
        return byte[].class.equals(typeClass);
    }

    /**
     * 获得包装类
     */
    @SuppressWarnings("all")
    public static Class<? extends Object> getBasicClass(Class typeClass) {
        Class _class = basicMap.get(typeClass);
        if (_class == null) {
            _class = typeClass;
        }
        return _class;
    }
}
