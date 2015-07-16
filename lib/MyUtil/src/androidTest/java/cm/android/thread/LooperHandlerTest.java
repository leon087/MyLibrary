package cm.android.thread;


import android.test.InstrumentationTestCase;

public class LooperHandlerTest extends InstrumentationTestCase {

    public void testGenerateTag() throws Exception {
        StackTraceElement caller = Thread.currentThread()
                .getStackTrace()[3];
        String tag = "%s:%s:%d";
        tag = String.format(tag, caller.getFileName(), caller.getMethodName(),
                caller.getLineNumber());
        if (tag.equals("%s:%s:%d")) {
            assertEquals(true, false);
        } else {
            assertEquals(true, true);
        }
    }
}
