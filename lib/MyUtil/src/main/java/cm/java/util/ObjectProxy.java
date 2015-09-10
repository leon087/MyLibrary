package cm.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class ObjectProxy {

    private static final Logger logger = LoggerFactory.getLogger("util");

    private Class<? extends Object> clazz; // 对象所属的类

    private Object target; // 目标对象

    public ObjectProxy(Object target) {
        rebindTarget(target); // 设置目标对象与方法
    }

    public void rebindTarget(Object target) {
        this.target = target;
        this.clazz = target.getClass();
    }

//    public <T> T doMethod(String methodName, Object... params) {
//        Method method = null;
//        if (params == null) {
//            method = getMethod(methodName);
//        } else {
//            int paramLength = params.length;
//            Class<?>[] paramTypes = new Class[paramLength];
//            for (int i = 0; i < paramLength; i++) {
//                paramTypes[i] = params[i].getClass();
//            }
//            method = getMethod(methodName, paramTypes);
//        }
//
//        if (method != null) {
//            return doMethod(method, params);
//        }
//
//        return null;
//    }

    public <T> T doMethod(Method method, Object... params) {
        return ReflectUtil.doMethod(target, method, params);
    }

    public Method getMethod(String methodName, Class<?>... parameterTypes) {
        return ReflectUtil.getMethod(clazz, methodName, parameterTypes);
    }
}
