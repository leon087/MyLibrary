package cm.android.framework.core.daemon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;

import java.io.File;
import java.io.IOException;

public class Daemon {

    private static final Logger logger = LoggerFactory.getLogger("Daemon");

    private static final String BIN_DIR_NAME = "bin";

    private static final String DAEMON_BIN_NAME = "daemon";

    /**
     * 一小时
     */
    public static final int INTERVAL_ON_SECOND = 60 * 60;

    private static final int MODE_RUN = 0;

    private static final int MODE_STOP = MODE_RUN + 1;

    /** start daemon */
    private static void execCommond(Context context, Class<?> daemonClazzName, int interval,
            int mode) {
        String cmd = context.getDir(BIN_DIR_NAME, Context.MODE_PRIVATE)
                .getAbsolutePath() + File.separator + DAEMON_BIN_NAME;

        /* create the command string */
        StringBuilder cmdBuilder = new StringBuilder();
        cmdBuilder.append(cmd);
        cmdBuilder.append(" -p ");
        cmdBuilder.append(context.getPackageName());
        cmdBuilder.append(" -s ");
        cmdBuilder.append(daemonClazzName.getName());
        cmdBuilder.append(" -t ");
        cmdBuilder.append(interval);
        cmdBuilder.append(" -m ");
        cmdBuilder.append(mode);

        try {
            Runtime.getRuntime().exec(cmdBuilder.toString()).waitFor();
        } catch (IOException | InterruptedException e) {
            logger.error("start daemon error: " + e.getMessage());
        }
    }

    /**
     * Run daemon process.
     *
     * @param context            context
     * @param daemonServiceClazz the name of daemon service class
     * @param interval           the interval to check
     */
    public static void startDaemon(final Context context, final Class<?> daemonServiceClazz,
            final int interval) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Command.install(context, BIN_DIR_NAME, DAEMON_BIN_NAME);
                execCommond(context, daemonServiceClazz, interval, MODE_RUN);
            }
        }).start();
    }

    public static void stopDaemon(final Context context, final Class<?> daemonServiceClazz,
            final int interval) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Command.install(context, BIN_DIR_NAME, DAEMON_BIN_NAME);
                execCommond(context, daemonServiceClazz, interval, MODE_STOP);
            }
        }).start();
    }
}
