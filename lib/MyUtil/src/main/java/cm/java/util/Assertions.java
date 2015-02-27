package cm.java.util;

import java.io.IOException;

public class Assertions {

    public static void checkState(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalStateException(String.valueOf(errorMessage));
        }
    }

    public static void checkArgumentForIO(boolean expression, String errorMessage)
            throws IOException {
        if (!expression) {
            throw new IOException(errorMessage);
        }
    }
}
