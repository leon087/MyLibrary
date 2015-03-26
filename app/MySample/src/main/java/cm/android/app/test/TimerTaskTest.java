package cm.android.app.test;

import java.util.Date;

import cm.android.timer.Timer;
import cm.android.util.MyFormatter;
import cm.android.timer.TimerTask;

public class TimerTaskTest {

    private static Timer timer = new Timer("ggg");

    public static void test() {
        timer.reset();

        android.util.Log.e("gggg", "gggg 1 begin");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                android.util.Log.e("gggg",
                        "gggg 1 time = " + MyFormatter.formatDate(System.currentTimeMillis()));
            }
        }, 1000, 5000);

        android.util.Log.e("gggg", "ggggggg 2 begin");
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                android.util.Log.e("gggg",
                        "ggggggg 2 time = " + MyFormatter.formatDate(System.currentTimeMillis()));
            }
        }, 0, 3000);

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                android.util.Log.e("gggg",
                        "ggggggggggggg 3 time = " + MyFormatter.formatDate(System.currentTimeMillis()));
            }
        }, new Date(2015, 3, 26, 19, 47));
    }

    public static void reset() {
        timer.cancel();
    }
}
