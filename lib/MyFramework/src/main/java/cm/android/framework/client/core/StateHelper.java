package cm.android.framework.client.core;

import android.content.Context;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import cm.java.util.IoUtil;

import static android.content.Context.MODE_PRIVATE;

public class StateHelper {

    private static final String STATE_FILE_NAME = "framework_app_status";

    private static final String TAG_STATE = "state";

    public static boolean isStateInit(Context context) {
        return readState(context);
    }

    public static void writeState(Context context, boolean state) {
        LogUtil.getLogger().info("StateHolder:writeState:state = " + state);

        Properties properties = new Properties();
        properties.setProperty(TAG_STATE, String.valueOf(state));

        OutputStream os = null;
        try {
            os = context.openFileOutput(STATE_FILE_NAME, MODE_PRIVATE);
            properties.store(os, "writeState:state = " + state);
        } catch (IOException e) {
            LogUtil.getLogger().error(e.getMessage());
        } finally {
            IoUtil.closeQuietly(os);
        }
    }

    public static boolean readState(Context context) {
        FileInputStream fis = null;
        try {
            fis = context.openFileInput(STATE_FILE_NAME);
            Properties properties = new Properties();
            properties.load(fis);

            boolean state = Boolean.valueOf(properties.getProperty(TAG_STATE, String.valueOf(false)));
            LogUtil.getLogger().info("StateHolder:readState:state = " + state);
            return state;
        } catch (IOException e) {
            LogUtil.getLogger().info("StateHolder:readState:state = false:" + e.getMessage());
            return false;
        } finally {
            IoUtil.closeQuietly(fis);
        }
    }
}
