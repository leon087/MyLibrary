package cm.android.sdk.content;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * 电池监听器
 */
public class BatteryReceiver extends BaseBroadcastReceiver {

    private BatteryChangedListener listener;

    public BatteryReceiver(BatteryChangedListener listener) {
        if (listener == null) {
            this.listener = defaultListener;
        } else {
            this.listener = listener;
        }
    }

    private static final BatteryChangedListener defaultListener = new BatteryChangedListener() {
        @Override
        public void batteryChanged(BatteryInfo batteryInfo) {
        }
    };

    @Override
    public void onReceive(Context context, Intent intent) {
        BatteryInfo batteryInfo = parseBattery(intent);

        listener.batteryChanged(batteryInfo);
    }

    @Override
    public IntentFilter createIntentFilter() {
        IntentFilter intentFilter = super.createIntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        return intentFilter;
    }

    public static interface BatteryChangedListener {

        public void batteryChanged(BatteryInfo batteryInfo);
    }

    public static class BatteryInfo {

        public int status;

        public int health;

        public boolean present;

        public int level;

        public int scale;

        public int icon_small;

        public int plugged;

        public int voltage;

        public int temperature;

        public String technology;

        @Override
        public String toString() {
            return "BatteryInfo{" +
                    "status=" + status +
                    ", health=" + health +
                    ", present=" + present +
                    ", level=" + level +
                    ", scale=" + scale +
                    ", icon_small=" + icon_small +
                    ", plugged=" + plugged +
                    ", voltage=" + voltage +
                    ", temperature=" + temperature +
                    ", technology='" + technology + '\'' +
                    '}';
        }
    }

    public static BatteryInfo getBattery(Context context) {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent intent = context.getApplicationContext().registerReceiver(null, filter);

        BatteryInfo batteryInfo = parseBattery(intent);
        return batteryInfo;
    }

    public static BatteryInfo parseBattery(Intent intent) {
        BatteryInfo batteryInfo = new BatteryInfo();
        if (intent == null) {
            return batteryInfo;
        }

        batteryInfo.status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);//API:5
        batteryInfo.health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
        batteryInfo.present = intent.getBooleanExtra(BatteryManager.EXTRA_PRESENT, false);
        batteryInfo.level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        batteryInfo.scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
        batteryInfo.icon_small = intent.getIntExtra(BatteryManager.EXTRA_ICON_SMALL, 0);
        batteryInfo.plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
        batteryInfo.voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
        batteryInfo.temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0);
        batteryInfo.technology = intent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);

        return batteryInfo;
    }

}