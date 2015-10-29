package cm.java.util;

import java.lang.reflect.InvocationHandler;

public class Proxy {

    public static <T> T newProxy(DynamicHandler hanlder) {
        Object obj = hanlder.getObj();
        Class clazz = obj.getClass();

        ClassLoader classLoader = clazz.getClassLoader();
        Class<T>[] interfaces = clazz.getInterfaces();
        return (T) java.lang.reflect.Proxy.newProxyInstance(classLoader, interfaces, hanlder);
    }

    public static abstract class DynamicHandler implements InvocationHandler {

        private Object obj;

        public DynamicHandler(Object obj) {
            this.obj = obj;
        }

        public Object getObj() {
            return obj;
        }
    }
}
