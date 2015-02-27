package cm.android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import cm.java.util.ReflectUtil;

public class BuildConfigUtil {

    private static final Logger logger = LoggerFactory.getLogger(BuildConfigUtil.class);

    public static Object getBuildConfigValue(Context context, String fieldName) {
        try {
            Class<?> clazz = Class.forName(context.getPackageName() + ".BuildConfig");
            return ReflectUtil.getStaticFieldValue(clazz, fieldName);
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
        } catch (NoSuchFieldException e) {
            logger.error(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public static boolean isDebug(Context context) {
        Object obj = getBuildConfigValue(context, "DEBUG");
        if (obj == null) {
            return false;
        }
        return (Boolean) obj;
    }
}
