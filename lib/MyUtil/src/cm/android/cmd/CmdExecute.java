package cm.android.cmd;

import cm.android.util.IoUtil;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

public class CmdExecute {

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
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		} finally {
			IoUtil.closeQuietly(inputStream);
			IoUtil.closeQuietly(outputStream);
			if (null != process) {
				process.destroy();
				process = null;
			}
		}
	}
}
