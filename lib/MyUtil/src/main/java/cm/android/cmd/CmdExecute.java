package cm.android.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import cm.android.util.IoUtil;

public final class CmdExecute {

    private static final Logger logger = LoggerFactory.getLogger("cmd");

    public static String run(String[] cmd, String workdirectory) {
        if (null == workdirectory) {
            return null;
        }

        ByteArrayOutputStream outputStream = null;
        InputStream inputStream = null;
        Process process = null;
        try {
            ProcessBuilder builder = new ProcessBuilder(cmd);
            // 设置一个路径
            builder.directory(new File(workdirectory));
            builder.redirectErrorStream(true);
            process = builder.start();
            inputStream = new BufferedInputStream(process.getInputStream());
            outputStream = new ByteArrayOutputStream();
            IoUtil.write(inputStream, outputStream);
            byte[] bytes = outputStream.toByteArray();
            return new String(bytes);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            IoUtil.closeQuietly(inputStream);
            IoUtil.closeQuietly(outputStream);
            if (null != process) {
                process.destroy();
            }
        }
    }

    public static String exec(String cmd) {
        ByteArrayOutputStream outputStream = null;
        InputStream inputStream = null;
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmd);
            inputStream = new BufferedInputStream(process.getInputStream());
            outputStream = new ByteArrayOutputStream();
            IoUtil.write(inputStream, outputStream);
            byte[] bytes = outputStream.toByteArray();
            return new String(bytes);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            IoUtil.closeQuietly(inputStream);
            IoUtil.closeQuietly(outputStream);
            if (null != process) {
                process.destroy();
            }
        }
    }
}
