package cm.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ReflectUtil {

    private static final Logger logger = LoggerFactory.getLogger("util");

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
     * GenricManager<Book>
     *
     * @param clazz The class to introspect
     * @return the first generic declaration, or <code>Object.class</code> if
     * cannot be determined
     */
    public static Type getSuperClassGenricType(Class clazz) {
        return getSuperClassGenricType(clazz, 0);
    }

    /**
     * 通过反射,获得定义Class时声明的父类的范型参数的类型. 如public BookManager extends
     * GenricManager<Book>
     *
     * @param clazz clazz The class to introspect
     * @param index the Index of the generic ddeclaration,start from 0.
     */
    public static Type getSuperClassGenricType(Class clazz, int index)
            throws IndexOutOfBoundsException {

        Type genType = clazz.getGenericSuperclass();

        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }

        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return params[index];
    }

    public static <T> T getFieldValue(Object object, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        T value = (T) field.get(object);
        return value;
    }

    public static void setFieldValue(Object object, String fieldName, Object filedValue)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, filedValue);
    }

    public static <T> T getStaticFieldValue(Class clazz, String fieldName)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        T value = (T) field.get(null);
        return value;
    }

    public static void setStaticFieldValue(Class clazz, String fieldName, Object filedValue)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(null, filedValue);
    }

    public static Method getMethod(Class clazz, String methodName, Class<?>... parameterTypes) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取方法上的注解
     *
     * @param anClazz 注解类
     */
    public static Annotation getAnnotation(Class anClazz, String methodName,
            Class<?>... parameterTypes) {
        Method method = getMethod(anClazz, methodName, parameterTypes);
        if (method != null) {
            return method.getAnnotation(anClazz);
        }

        return null;
    }

    public static <T> T doMethod(Object target, Method method, Object... params) {
        try {
            return (T) method.invoke(target, params);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            return null;
        } catch (InvocationTargetException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }
}
