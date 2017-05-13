package cm.java.util;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public final class Reflect {

    public final static class ReflectException extends RuntimeException {

        private static final long serialVersionUID = -2541705421184545544L;

        public ReflectException(String message) {
            super(message);
        }

        public ReflectException(String message, Throwable cause) {
            super(message, cause);
        }

        public ReflectException() {
            super();
        }

        public ReflectException(Throwable cause) {
            super(cause);
        }
    }

    public static class MethodWrapper {
        private Method method;
        private Object obj;

        MethodWrapper(Object obj, Method method) {
            this.method = method;
            this.obj = obj;
        }

        public <T> T call(Object... args) throws ReflectException {
            try {
                checkAccessible(method);
                return (T) method.invoke(obj, args);
            } catch (Exception e) {
                throw new ReflectException(e);
            }
        }
    }

    public static Reflect load(String name) throws ReflectException {
        return load(forName(name));
    }

    public static Reflect load(String name, ClassLoader classLoader) throws ReflectException {
        return load(forName(name, classLoader));
    }

    public static Reflect load(Class<?> clazz) {
        return new Reflect(clazz);
    }

    public static Reflect bind(Object instance) {
        return new Reflect(instance);
    }

    private final Object clazz;
    private Object instance;

    private Reflect(Class clazz) {
        this.clazz = clazz;
        this.instance = null;
    }

    private Reflect(Object instance) {
        this.clazz = instance.getClass();
        this.instance = instance;
    }

    public Reflect field(String name, Object value) throws ReflectException {
        try {
            Field field = getField(name);
            checkAccessible(field);
            if (Modifier.isStatic(field.getModifiers())) {
                field.set(clazz, value);
            } else {
                field.set(instance, value);
            }
            return this;
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    public <T> T field(String name) throws ReflectException {
        try {
            Field field = getField(name);
            checkAccessible(field);
            if (Modifier.isStatic(field.getModifiers())) {
                return (T) field.get(clazz);
            } else {
                return (T) field.get(instance);
            }
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    private static void checkAccessible(AccessibleObject accessibleObject) {
        if (!accessibleObject.isAccessible()) {
            accessibleObject.setAccessible(true);
        }
    }

    private Field getField(String name) throws ReflectException {
        Class<?> type = type();

        Exception e = null;
        do {
            try {
                Field field = type.getDeclaredField(name);
                return field;
            } catch (NoSuchFieldException ignore) {
                if (e == null) {
                    e = ignore;
                }
            }

            type = type.getSuperclass();
        } while (type != null);

        throw new ReflectException(e);
    }

//    @Deprecated
//    public <T> T method(String name, Object... args) throws ReflectException {
//        Class<?>[] types = types(args);
//
//        try {
//            Method method = getMethod(name, types);
//            if (Modifier.isStatic(method.getModifiers())) {
//                return (T) method.invoke(clazz, args);
//            } else {
//                return (T) method.invoke(instance, args);
//            }
//        } catch (Exception e) {
//            throw new ReflectException(e);
//        }
//    }

//    public <T> T method(String name) throws ReflectException {
//        Class<?>[] types = new Class[0];
//        return method(name, types);
//    }

//    public <T> T method(String name, Class<?>[] types, Object... args) throws ReflectException {
//        // Class<?>[] types = types(args);
//
//        try {
//            Method method = getMethod(name, types);
//            if (Modifier.isStatic(method.getModifiers())) {
//                return (T) method.invoke(clazz, args);
//            } else {
//                return (T) method.invoke(instance, args);
//            }
//        } catch (Exception e) {
//            throw new ReflectException(e);
//        }
//    }

    public <T> T call(String name) throws ReflectException {
        return method(name).call();
    }

    public MethodWrapper method(String name, Class<?>... types) throws ReflectException {
        try {
            Method method = getMethod(name, types);
            if (Modifier.isStatic(method.getModifiers())) {
                return new MethodWrapper(clazz, method);
            } else {
                return new MethodWrapper(instance, method);
            }
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    private Method getMethod(String name, Class<?>[] types) throws NoSuchMethodException {
        Class<?> type = type();

        Exception e = null;
        do {
            try {
                return type.getDeclaredMethod(name, types);
            } catch (NoSuchMethodException ignore) {
                if (e == null) {
                    e = ignore;
                }
            }

            type = type.getSuperclass();
        } while (type != null);

        throw new NoSuchMethodException();
    }

    public Reflect newInstance() throws ReflectException {
        Class<?>[] types = new Class[0];
        return newInstance(types);
    }

    public Reflect newInstance(Class<?>[] types, Object... args) throws ReflectException {
        // Class<?>[] types = types(args);
        try {
            Constructor<?> constructor = type().getDeclaredConstructor(types);
            checkAccessible(constructor);
            instance = constructor.newInstance(args);
            return this;
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    private static Class<?> forName(String name) throws ReflectException {
        try {
            return Class.forName(name);
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    private static Class<?> forName(String name, ClassLoader classLoader) throws ReflectException {
        try {
            return Class.forName(name, true, classLoader);
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    private Class<?> type() {
        return (Class<?>) clazz;
    }
}