package cm.android.framework.client.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtil {
    private static final Logger logger = LoggerFactory.getLogger("framework");

    public static Logger getLogger() {
        return logger;
    }
}
