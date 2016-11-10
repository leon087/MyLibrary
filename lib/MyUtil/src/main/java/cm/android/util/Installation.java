package cm.android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

public class Installation {

    private static final Logger logger = LoggerFactory.getLogger("Installation");

    private static String sID;
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String id(@NonNull Context context) {
        if (sID == null) {
            File installation = new File(context.getFilesDir(), INSTALLATION);
            try {
                if (!installation.exists()) {
                    writeInstallationFile(installation);
                }
                sID = readInstallationFile(installation);
            } catch (IOException e) {
                logger.error("Couldn't retrieve InstallationId for " + context.getPackageName(), e);
                return "";
            } catch (RuntimeException e) {
                logger.error("Couldn't retrieve InstallationId for " + context.getPackageName(), e);
                return "";
            }
        }
        return sID;
    }

    @NonNull
    private static String readInstallationFile(File installation) throws IOException {
        RandomAccessFile f = new RandomAccessFile(installation, "r");
        byte[] bytes = new byte[(int) f.length()];
        try {
            f.readFully(bytes);
        } finally {
            f.close();
        }
        return new String(bytes);
    }

    private static void writeInstallationFile(@NonNull File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        try {
            String id = UUID.randomUUID().toString();
            out.write(id.getBytes());
        } finally {
            out.close();
        }
    }
}
