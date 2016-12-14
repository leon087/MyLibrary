package cm.android.log;

import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.charset.Charset;

import cm.android.log.MyLog.MyLogManager;
import cm.android.log.MyLog.MyLogManager.Level;
import cm.android.log.MyLog.MyLogManager.LogMode;
import cm.android.util.EnvironmentUtil;
import cm.android.util.MyFormatter;
import cm.java.util.IoUtil;

/**
 * android Log的Adapter类，打印调试信息。
 */
@Deprecated
public class MyLog {

    private MyLog() {
    }

    /**
     * 设置日志级别
     */
    public static void setLogLevel(Level level) {
        MyLogManager.getLogger().logLevel = level;
    }

    /**
     * 设置日志记录模式
     */
    public static void setLogMode(LogMode mode) {
        if (mode != null) {
            MyLogManager.getLogger().logMode = mode;
        }
    }

    /**
     * 初始化
     */
    public static void initialize(String traceTag) {
        MyLogManager.getLogger().init(traceTag);
    }

    /**
     * 释放
     */
    public static void release() {
        MyLogManager.getLogger().deInit();
    }

    /**
     * 打印{@link Level#DEBUG}级别的日志
     */
    public static void d(String msg) {
        MyLogManager.getLogger().log(Level.DEBUG, msg);
    }

    // public void d(String msg) {
    // log(Level.DEBUG, msg);
    // }

    /**
     * 打印{@link Level#INFO}级别的日志
     */
    public static void i(String msg) {
        MyLogManager.getLogger().log(Level.INFO, msg);
    }

    /**
     * 打印{@link Level#ERROR}级别的日志
     */
    public static void e(String msg) {
        MyLogManager.getLogger().log(Level.ERROR, msg);
    }

    /**
     * 打印{@link Level#ERROR}级别的日志
     */
    public static void e(Throwable tr) {
        MyLogManager.getLogger().log(Level.ERROR,
                android.util.Log.getStackTraceString(tr));
    }

    /**
     * 打印{@link Level#ERROR}级别的日志
     */
    public static void e(String msg, Throwable tr) {
        MyLogManager.getLogger().log(
                Level.ERROR,
                msg + System.getProperty("line.separator")
                        + android.util.Log.getStackTraceString(tr));
    }

    /**
     * 判断是否为DEBUG级别
     */
    public static boolean isDebug() {
        // 设置的level比debug小，则可以记录debug日志
        return MyLogManager.getLogger().logLevel.getLevel() <= Level.DEBUG
                .getLevel();
    }

    /**
     * 日志管理类
     */
    public static class MyLogManager {

        volatile String MY_TRACE = "MY_TRACE: ";

        // 可配参数
        Level logLevel = Level.MAX;

        LogMode logMode = LogMode.LOGCAT;

        // logger
        private LogcatLogger logcatLogger = new LogcatLogger();

        private FileLogger fileLogger = new FileLogger(logcatLogger);

        private MyLogManager() {
        }

        static MyLogManager getLogger() {
            return SingletonHolder.INSTANCE;
        }

        public String getMyTrace() {
            return MY_TRACE;
        }

        void log(Level level, String msg) {
            if (level.getLevel() < logLevel.getLevel()) {
                return;
            }

            // logcat
            logcatLogger.log(level, msg);

            if (isMode(LogMode.FILE)) {
                fileLogger.log(level, msg);
            }
        }

        void init(String traceTag) {
            MY_TRACE = traceTag + ": ";
            if (isMode(LogMode.FILE)) {
                fileLogger.initSdcardMode();
            }

            String format = "MODE = %s,LEVEL = %s";
            log(Level.INFO, String.format(format, logMode, logLevel));
        }

        void deInit() {
            if (isMode(LogMode.FILE)) {
                fileLogger.releaseSdcardMode();
            }
        }

        private boolean isMode(LogMode mode) {
            // return (MODE & mode) == mode;
            return logMode == mode;
        }

        /**
         * 日志记录模式 {@link #LOGCAT}和{@link #FILE}
         */
        public static enum LogMode {
            /**
             * logcat方式输出
             */
            LOGCAT,
            /**
             * 日志写入到本地文件
             */
            FILE;

            private LogMode() {
            }
        }

        /**
         * 日志记录级别
         */
        public static enum Level {
            /**
             * DEBUG
             */
            DEBUG("[DEBUG] - ", android.util.Log.DEBUG),
            /**
             * INFO
             */
            INFO("[INFO] - ", android.util.Log.INFO),
            /**
             * ERROR
             */
            ERROR("[ERROR] - ", android.util.Log.ERROR),
            /**
             * MAX，不记录日志
             */
            MAX("", Integer.MAX_VALUE);

            private String tag = null;

            private int level = 0;

            private Level(String tag, int level) {
                this.tag = tag;
                this.level = level;
            }

            public int getLevel() {
                return level;
            }

            public String getTag() {
                return tag;
            }
        }

        private static final class SingletonHolder {

            private static final MyLogManager INSTANCE = new MyLogManager();
        }
    }

}

abstract class MyBaseLogger {

    private static String generateTag(StackTraceElement caller) {
        String tag = "[ %s:%s:%s():%d ]";
        String callerFileName = caller.getFileName();
        String fileName = callerFileName;
        // if (null != callerFileName) {
        // fileName = callerFileName.substring(0,
        // callerFileName.lastIndexOf('.'));
        // }
        tag = String.format(tag, Thread.currentThread().getName(), fileName,
                caller.getMethodName(), caller.getLineNumber());
        return tag;
    }

    protected String getTag() {
        StringBuilder sb = new StringBuilder();
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.getClassName().equals(MyBaseLogger.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(MyLogManager.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(MyLog.class.getName())) {
                continue;
            }

            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(this.getClass().getName())) {
                continue;
            }

            sb.append(generateTag(st));
            return sb.toString();
        }
        return null;
    }

    /**
     * 运行UT时打印一行信息
     */
    protected void printLine(String tag, String msg) {
        StringBuilder sb = new StringBuilder();
        sb.append(tag);
        sb.append(" ");
        sb.append(msg);
        System.out.println(sb.toString());
    }

    public abstract void log(Level level, String msg);
}

class LogcatLogger extends MyBaseLogger {

    @Override
    public void log(Level level, String msg) {
        String tag = getTag();
        String msgStr = msg;
        // String tag = MyLogManager.MY_TRACE;
        // String msgStr = getTag() + " - " + msg;
        try {
            if (Level.DEBUG == level) {
                android.util.Log.d(tag, msgStr);
            } else if (Level.INFO == level) {
                android.util.Log.i(tag, msgStr);
            } else if (Level.ERROR == level) {
                android.util.Log.e(tag, msgStr);
            }
        } catch (Exception e) {
            printLine(tag, msgStr);
        }
    }
}

class FileLogger extends MyBaseLogger {

    private static final int BUFFER_SIZE = 1024 * 100;

    private static final String FILE_EXT = ".log";

    private volatile boolean initSdcardSuccess = false;

    private File logFile;

    private BufferedOutputStream outputStream = null;

    private StringBuilder builder = new StringBuilder();

    private LogcatLogger logcatLogger;

    public FileLogger(LogcatLogger logcatLogger) {
        this.logcatLogger = logcatLogger;
    }

    private String getName() {
        String fileNameFormat = "%s_%s.%s";
        String fileName = String.format(fileNameFormat, MyLogManager.getLogger().getMyTrace(),
                getCurrDateStr(), FILE_EXT);
        return fileName;
    }

    public void initSdcardMode() {
        if (initSdcardSuccess) {
            return;
        }

        logFile = Environment.getExternalStorageDirectory();
        if (!EnvironmentUtil.isExternalStorageWritable()) {
            logFile = null;
        }

        if (logFile == null) {
            return;
        }

        logFile = new File(logFile, getName());

        boolean append = true;
        // 是否存在文件
        if (!logFile.exists()) {
            IoUtil.createFile(logFile);
            append = false;
        }

        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(
                    logFile, append), BUFFER_SIZE);
            if (null == outputStream) {
                logcatLogger.log(Level.ERROR, "Open logfile failed!");
                return;
            }
            initSdcardSuccess = true;
        } catch (Exception e) {
            logcatLogger.log(Level.ERROR, "Open logfile exception!");
            e.printStackTrace();
        }
    }

    public void releaseSdcardMode() {
        initSdcardSuccess = false;
        if (null != outputStream) {
            try {
                outputStream.flush();
                outputStream.close();
                outputStream = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private String getCurrDateStr() {
        return MyFormatter.formatDate("yyyyMMdd", System.currentTimeMillis());
    }

    public synchronized void log(Level level, String msg) {
        if (!initSdcardSuccess) {
            logcatLogger.log(Level.ERROR, "Try open logfile.");
            initSdcardMode();
        }

        if (null != outputStream) {
            String str = formatLogStr(level, MyLogManager.getLogger().getMyTrace(), msg);

            try {
                outputStream.write(str.getBytes(Charset.defaultCharset()));
            } catch (Exception e) {
                initSdcardSuccess = false;
                logcatLogger.log(Level.ERROR,
                        android.util.Log.getStackTraceString(e));
                e.printStackTrace();
            }
        }
    }

    private String formatLogStr(Level level, String tag, String msg) {
        // 格式：[INFO] - YYYY-MM-DD HH:MM:SS.SSS :[tag] msg
        builder.setLength(0);
        builder.append(level.getTag());
        builder.append(
                MyFormatter.formatDate("yyyy-MM-dd HH:mm:ss.SSS", System.currentTimeMillis()));
        builder.append(" : [");
        builder.append(tag);
        builder.append("] ");
        builder.append(getTag());
        builder.append(" - ");
        builder.append(msg);
        builder.append(System.getProperty("line.separator"));

        return builder.toString();
    }
}
