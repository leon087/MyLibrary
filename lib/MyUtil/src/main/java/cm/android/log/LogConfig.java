package cm.android.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.android.BasicLogcatConfigurator;
import ch.qos.logback.classic.android.LogcatAppender;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.rolling.RollingFileAppender;
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy;

/**
 */
public class LogConfig {

    public static void test() {
        configLogback(Level.OFF, new File("/sdcard"));
        //LogConfig.configLogbackByString(LogConfig.genXml(LogLevel.OFF,new File("/sdcard")));
        // BasicLogcatConfigurator.configureDefaultContext();

        Logger log = LoggerFactory.getLogger(LogConfig.class);
        log.debug("gggg1,{},{}", "ggggg2", "ggggg3");
        log.info("gggg1,{},{}", "ggggg2", "ggggg3");
        log.error("gggg1,{},{}", "ggggg2", "ggggg3");
    }

    private static volatile boolean configFlag = false;

    private static final String PATTERN_LOGCAT = "[%logger:%thread:%file:%method:%line] %msg%n";

    private static final String PATTERN_FILE
            = "%date %-5level [%logger:%thread:%file:%method:%line] - %msg%n";

    public static void configLogback(Level level, File logDir) {
        if (configFlag) {
            return;
        }
        configFlag = true;

        // add the newly created appenders to the root logger;
        // qualify Logger to disambiguate from org.slf4j.Logger
        ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory
                .getLogger(Logger.ROOT_LOGGER_NAME);

        // reset the default context (which may already have been initialized)
        // since we want to reconfigure it
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.reset();

        if (logDir != null) {
            setFileMode(root, lc, logDir);
        }

        setLogcatMode(root, lc);

        // Level:OFF
        root.setLevel(level);
    }

    private static void setFileMode(ch.qos.logback.classic.Logger root,
            LoggerContext lc, File logDir) {
        // setup FileAppender
        PatternLayoutEncoder encoder1 = new PatternLayoutEncoder();
        encoder1.setContext(lc);

        encoder1.setPattern(PATTERN_FILE);
        encoder1.start();

        RollingFileAppender<ILoggingEvent> fileAppender = new RollingFileAppender<ILoggingEvent>();
        fileAppender.setContext(lc);
        fileAppender.setFile(logDir.getAbsolutePath() + File.separator
                + "logback.log");
        fileAppender.setEncoder(encoder1);

        TimeBasedRollingPolicy<ILoggingEvent> policy = new TimeBasedRollingPolicy<ILoggingEvent>();
        policy.setContext(lc);
        policy.setMaxHistory(7);
        policy.setFileNamePattern(logDir.getAbsolutePath() + File.separator
                + "logback.%d{yyyy-MM-dd}.log.gz");
        policy.setParent(fileAppender);
        policy.start();

        fileAppender.setRollingPolicy(policy);
        fileAppender.start();

        root.addAppender(fileAppender);
    }

    private static void setLogcatMode(ch.qos.logback.classic.Logger root,
            LoggerContext lc) {
        // setup LogcatAppender
        PatternLayoutEncoder encoder2 = new PatternLayoutEncoder();
        encoder2.setContext(lc);
        encoder2.setPattern(PATTERN_LOGCAT);
        encoder2.start();

        LogcatAppender logcatAppender = new LogcatAppender();
        logcatAppender.setContext(lc);
        logcatAppender.setEncoder(encoder2);
        logcatAppender.start();

        root.addAppender(logcatAppender);
    }

    public static String genXml(LogLevel level, File logDir) {
        String xmlConfig = "<configuration>" +
                genFileString(logDir) +

                "<appender name='LOGCAT' class='ch.qos.logback.classic.android.LogcatAppender'>" +
                "<encoder>" +
                "<pattern>" + PATTERN_LOGCAT + "</pattern>" +
                "</encoder>" +
                "</appender>" +

                "<root level='" + level.getLevel() + "'>" +
                genFileAppenderRef(logDir) +
                "<appender-ref ref='LOGCAT' />" +
                "</root>" +

                "</configuration>";
        return xmlConfig;
    }

    public static enum LogLevel {
        ALL("ALL"), DEBUG("DEBUG"), INFO("INFO"), ERROR("ERROR"), OFF("OFF");

        private String levelStr;

        private LogLevel(String levelStr) {
            this.levelStr = levelStr;
        }

        public String getLevel() {
            return levelStr;
        }
    }

    private static String genFileString(File logDir) {
        if (logDir == null) {
            return "";
        }

        String fileXmlConfig = "<property name='LOG_DIR' value='" + logDir.getAbsolutePath() + "'/>"
                +

                "<appender name='FILE' class='ch.qos.logback.core.rolling.RollingFileAppender'>" +
                "<file>${LOG_DIR}/logback.log</file>" +

                "<rollingPolicy class='ch.qos.logback.core.rolling.TimeBasedRollingPolicy'>" +
                "<fileNamePattern>${LOG_DIR}/logback.%d{yyyy-MM-dd}.log.gz</fileNamePattern>" +
                "<maxHistory>7</maxHistory>" +
                "</rollingPolicy>" +

                "<encoder>" +
                "<pattern>" + PATTERN_FILE + "</pattern>" +
                "</encoder>" +

                "</appender>";
        return fileXmlConfig;
    }

    private static String genFileAppenderRef(File logDir) {
        if (logDir == null) {
            return "";
        }
        return "<appender-ref ref='FILE' />";
    }

    public static void configLogbackByString(String xmlString) {
        // reset the default context (which may already have been initialized)
        // since we want to reconfigure it
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        lc.reset();

        JoranConfigurator config = new JoranConfigurator();
        config.setContext(lc);

        try {
            InputStream stream = new ByteArrayInputStream(xmlString.getBytes());
            config.doConfigure(stream);
        } catch (Exception e) {
            e.printStackTrace();
            BasicLogcatConfigurator.configureDefaultContext();
        }
    }
}
