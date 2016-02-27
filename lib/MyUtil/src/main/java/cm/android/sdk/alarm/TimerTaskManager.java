package cm.android.sdk.alarm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.Map;

import cm.android.sdk.content.BaseBroadcastReceiver;
import cm.android.util.IntentUtil;
import cm.java.util.ObjectUtil;

public final class TimerTaskManager {

    private static final Logger logger = LoggerFactory.getLogger("timer");

    private final Map<String, TimerTask> taskMap = ObjectUtil.newHashMap();

    private final TimerReceiver timerReceiver = new TimerReceiver();

    private Context context;

    public void register(String action, long period) {
        register(action, period, 0);
    }

    public void register(String action, long period, long delay) {
        register(action, period, delay, true);
    }

    public synchronized void register(String action, long period, long delay,
            boolean globalBroadcast) {
        if (taskMap.get(action) != null) {
            return;
        }

        TimerTask alarmTask = new MyAlarmTask(action, period, globalBroadcast);
        taskMap.put(action, alarmTask);
        alarmTask.start(context, delay);

        logger.info("action = " + action);
    }

    public synchronized void unregister(String action) {
        TimerTask task = taskMap.remove(action);
        if (task != null) {
            task.cancel(context);
        }

        logger.info("action = " + action);
    }

    public void start(Context context) {
        if (this.context != null) {
            return;
        }
        this.context = context;
        taskMap.clear();
        timerReceiver.register(context);

        logger.info("TimerTaskManager:start");
    }

    public void stop() {
        if (context == null) {
            return;
        }
        taskMap.clear();
        timerReceiver.unregister(context);
        this.context = null;

        logger.info("TimerTaskManager:stop");
    }

    private class MyAlarmTask extends TimerTask {

        private String action;

        private long period;

        private boolean globalBroadcast;

        MyAlarmTask(String action, long period, boolean globalBroadcast) {
            this.action = action;
            this.period = period;
            this.globalBroadcast = globalBroadcast;
        }

        @Override
        protected Intent getIntent(Context context) {
            Intent alarmIntent = new Intent(TimerReceiver.ACTION_ALARM_TASK);
            alarmIntent.setPackage(context.getPackageName());
//                alarmIntent.putExtra(AlarmTaskReceiver.INTENT_DATA, intent.getExtras());
            alarmIntent.putExtra(TimerReceiver.INTENT_ACTION, action);
            alarmIntent.putExtra(TimerReceiver.INTENT_BROADCAST_TYPE, globalBroadcast);
            return alarmIntent;
        }

        @Override
        protected long getDelayAtMillis() {
            return period;
        }

        @Override
        public int getRequestCode() {
            return hashCode();
        }
    }

    private class TimerReceiver extends BaseBroadcastReceiver {

        public static final int LOCAL_BROADCAST = 1;

        public static final String ACTION_ALARM_TASK = "cm.android.sdk.alarm.AlarmTaskReceiver";

        public static final String INTENT_DATA_DELAY = "intent_data_delay";

        public static final String INTENT_ACTION = "action";

        public static final String INTENT_BROADCAST_TYPE = "broadcastType";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getStringExtra(INTENT_ACTION);
            String alarmAction = action;
            TimerTask task = taskMap.get(alarmAction);
            long delay = 0;
            if (task != null) {
                task.schedule(context);
                delay = task.getDelayAtMillis();
            }

            Intent realIntent = new Intent(action);
            realIntent.putExtra(INTENT_DATA_DELAY, delay);
            boolean globalBroadcast = intent.getBooleanExtra(INTENT_BROADCAST_TYPE, true);
            if (globalBroadcast) {
                IntentUtil.sendBroadcastInternal(context, realIntent);
            } else {
                IntentUtil.sendBroadcastLocal(context, realIntent);
            }

            logger.info("alarmAction = " + alarmAction);
        }

        @Override
        public IntentFilter createIntentFilter() {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_ALARM_TASK);
            return filter;
        }
    }
}
