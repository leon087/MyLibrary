package cm.android.util;

import java.lang.reflect.Method;

public class SystemPropertiesProxy {

    private static final SystemPropertiesProxy SINGLETON = new SystemPropertiesProxy(null);

    private Class<?> SystemProperties;

    private Method getString, getBoolean;

    private SystemPropertiesProxy(ClassLoader cl) {
        try {
            setClassLoader(cl);
        } catch (Exception e) {
        }
    }

    private static SystemPropertiesProxy getInstance() {
        return SINGLETON;
    }

    public void setClassLoader(ClassLoader cl)
            throws ClassNotFoundException, SecurityException, NoSuchMethodException {
        if (cl == null) {
            cl = this.getClass().getClassLoader();
        }
        SystemProperties = cl.loadClass("android.os.SystemProperties");
        getString = SystemProperties.getMethod("get", new Class[]{String.class, String.class});
        getBoolean = SystemProperties.getMethod("getBoolean",
                new Class[]{String.class, boolean.class});
    }

    private String getInternal(String key, String def) throws IllegalArgumentException {
        if (SystemProperties == null || getString == null) {
            return null;
        }

        String ret = null;
        try {
            ret = (String) getString.invoke(SystemProperties, new Object[]{key, def});
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
        }
        // if return value is null or empty, use the default
        // since neither of those are valid values
        if (ret == null || ret.length() == 0) {
            ret = def;
        }
        return ret;
    }

    public Boolean getBoolean(String key, boolean def)
            throws IllegalArgumentException {

        if (SystemProperties == null || getBoolean == null) {
            return def;
        }

        Boolean ret = def;
        try {
            ret = (Boolean) getBoolean.invoke(SystemProperties, new Object[]{key, def});
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
        }
        return ret;
    }

    public static String get(String key, String def) {
        return getInstance().getInternal(key, def);
    }
}
