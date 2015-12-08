package cm.android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import cm.java.util.ReflectUtil;

public class BuildConfigUtil {

    private static final Logger logger = LoggerFactory.getLogger(BuildConfigUtil.class);

    public static <T> T getBuildConfigValue(Context context, String fieldName, T defValue) {
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

        return defValue;
    }

    public static boolean isDebug(Context context) {
        Boolean debug = getBuildConfigValue(context, "DEBUG", false);
        return debug;
    }
}
