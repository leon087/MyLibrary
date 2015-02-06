package cm.android.app.test.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

import cm.android.sdk.content.BaseBroadcastReceiver;

public class TestReceiver2 extends BaseBroadcastReceiver {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    public static final String ACTION = "cm.android.ggg.test2";

    @Override
    public IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        return filter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        logger.error("ggg time = {},intent = {}", SystemClock.elapsedRealtime(), intent);
    }
}
