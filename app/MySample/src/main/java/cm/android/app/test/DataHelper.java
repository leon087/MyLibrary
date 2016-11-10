package cm.android.app.test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import cm.android.app.core.ObjectPool;
import cm.java.util.IoUtil;
import cm.java.util.Utils;

public class DataHelper {

    private static final String STATE_FILE_NAME = "prefs";

    private static Properties properties;

    private static void write(String key, String value) {
        File file = new File(ObjectPool.getAppContext().getFilesDir(), STATE_FILE_NAME);
        Properties properties = IoUtil.loadProperties(file);
        properties.setProperty(key, value);

        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            properties.store(os, "");
        } catch (IOException e) {
        } finally {
            IoUtil.closeQuietly(os);
        }
    }

    private void commit() {

    }

    private static String read(String key) {
        File file = new File(ObjectPool.getAppContext().getFilesDir(), STATE_FILE_NAME);
        if (!file.exists()) {
            return "";
        }

        Properties properties = IoUtil.loadProperties(file);
        String value = properties.getProperty(key, "");
        return value;
    }

    public static String[] readArray(File file, String key) {
        if (!file.exists()) {
            return new String[]{};
        }

        Properties properties = IoUtil.loadProperties(file);
        String value = properties.getProperty(key);
        if (Utils.isEmpty(value)) {
            return null;
        }
        return value.split(",");
    }
}
