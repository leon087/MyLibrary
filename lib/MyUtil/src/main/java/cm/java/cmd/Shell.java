package cm.java.cmd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import cm.java.util.IoUtil;

public final class Shell {

    private static final Logger logger = LoggerFactory.getLogger("cmd");

    public static final String SU = "su";

    public static final String SH = "sh";

    private static final String COMMAND_EXIT = "exit\n";

    private static final String COMMAND_LINE_END = "\n";

    public static String exec(String command) {
        return exec(SH, new String[]{command});
    }

    public static String exec(String[] commands) {
        return exec(SH, commands);
    }

    public static String exec(String shell, String[] commands) {
        StringBuilder output = new StringBuilder();

        Process process = null;
        BufferedOutputStream shellInput = null;
        BufferedReader shellOutput = null;

        try {
            process = Runtime.getRuntime().exec(shell);

            shellInput = new BufferedOutputStream(process.getOutputStream());
            shellOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));

            for (String command : commands) {
                logger.debug("command: " + command);
                shellInput.write((command + " 2>&1" + COMMAND_LINE_END).getBytes(Charset.defaultCharset()));
            }

            shellInput.write(COMMAND_EXIT.getBytes(Charset.defaultCharset()));
            shellInput.flush();

            String line;
            while ((line = shellOutput.readLine()) != null) {
                logger.debug("command output: " + line);
                output.append(line);
            }

            process.waitFor();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            IoUtil.closeQuietly(shellInput);
            IoUtil.closeQuietly(shellOutput);

            if (process != null) {
                process.destroy();
            }
        }

        return output.toString();
    }

    public static Result exec2(String shell, String[] commands) {
        StringBuilder output = new StringBuilder();
        StringBuilder error = new StringBuilder();

        Process process = null;
        BufferedOutputStream shellInput = null;
        BufferedReader shellOutput = null;
        BufferedReader shellError = null;

        Result result = new Result();

        try {
            process = Runtime.getRuntime().exec(shell);

            shellInput = new BufferedOutputStream(process.getOutputStream());
            shellOutput = new BufferedReader(new InputStreamReader(process.getInputStream()));
            shellError = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            for (String command : commands) {
                logger.debug("command: " + command);
                shellInput.write(command.getBytes(Charset.defaultCharset()));
                shellInput.write(COMMAND_LINE_END.getBytes(Charset.defaultCharset()));
            }

            shellInput.write(COMMAND_EXIT.getBytes(Charset.defaultCharset()));
            shellInput.flush();

            String line;
            while ((line = shellOutput.readLine()) != null) {
                logger.debug("command output: " + line);
                output.append(line);
            }

            while ((line = shellError.readLine()) != null) {
                logger.debug("command error: " + line);
                error.append(line);
            }

            result.exit = process.waitFor();
            result.output = output.toString();
            result.error = error.toString();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } finally {
            IoUtil.closeQuietly(shellInput);
            IoUtil.closeQuietly(shellOutput);
            IoUtil.closeQuietly(shellError);

            if (process != null) {
                process.destroy();
            }
        }

        return result;
    }

    public static class Result {

        public int exit;

        public String output;

        public String error;
    }

    public static boolean su() {
        return exec2(SU, new String[]{"echo root"}).exit == 0;
    }

}
