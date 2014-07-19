package cm.android.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogTest {
    private final Logger log = LoggerFactory.getLogger(LogTest.class);

    public void myMethod() {
        log.info("This message should be seen in log file and logcat");
    }
}
