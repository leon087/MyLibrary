package cm.android.app.test.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;

import cm.android.sdk.alarm.BaseAlarmReceiver;

public class TestReceiver extends BaseAlarmReceiver {

    private static final Logger logger = LoggerFactory.getLogger("ggg");

    public static final String ACTION = "cm.android.ggg";

    @Override
    public void onHandleIntent(Context context, Intent intent) {
        logger.error("ggg time = {},intent = {}", SystemClock.elapsedRealtime(), intent);
    }

    @Override
    public IntentFilter createIntentFilter() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION);
        return filter;
    }

    @Override
    protected Intent getIntent() {
        Intent intent = new Intent();
        intent.setAction(TestReceiver.ACTION);
        return intent;
    }

    @Override
    protected long getDelayAtMillis() {
        return 1000 * 10;
    }
}
