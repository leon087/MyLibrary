package cm.android.util;

import android.content.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildConfigUtil {
    private static final Logger logger = LoggerFactory.getLogger(BuildConfigUtil.class);

    public static <T> T getBuildConfigValue(Context context, String fieldName) {
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
        boolean debug = getBuildConfigValue(context, "DEBUG");
        return debug;
    }
}
