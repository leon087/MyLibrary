package cm.android.sdk.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * sdcard监听类
 */
public class SdcardManager {

    private static final Logger logger = LoggerFactory.getLogger(SdcardManager.class);

    private class ExternalStorageReceiver extends BaseBroadcastReceiver {

        @Override
        public IntentFilter createIntentFilter() {
            IntentFilter intentFilter = super.createIntentFilter();
            intentFilter.addAction(Intent.ACTION_MEDIA_MOUNTED);//sd卡被插入，且已经挂载
            intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);//sd卡被移除
            intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);//sd卡存在，但还没有挂载
            intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);//sd卡已经从sd卡插槽拔出，但是挂载点还没解除
            intentFilter.addAction(Intent.ACTION_MEDIA_SHARED);//sd卡作为USB大容量存储被共享，挂载被解除
            intentFilter.addAction(Intent.ACTION_MEDIA_EJECT);//物理的拔出 SDCARD
            intentFilter.setPriority(1000);
            intentFilter.addDataScheme("file"); // 必须要有此行，否则无法收到广播
            return intentFilter;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            logger.info("intent = " + intent);
            String action = intent.getAction();

            if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
                // SD卡成功挂载
                listener.onMounted();
            } else if (Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
                // SD卡挂载失败
                listener.onUnmounted();
            }
        }
    }

    /**
     * sdcard监听接口
     */
    public static interface ExternalStorageListener {

        /**
         * sdcard已挂载
         */
        void onMounted();

        /**
         * sdcard已被移除
         */
        void onUnmounted();
    }


    private ExternalStorageReceiver receiver = new ExternalStorageReceiver();

    private Context context;

    private ExternalStorageListener listener;

    /**
     * 初始化
     */
    public void init(Context context, ExternalStorageListener listener) {
        this.context = context;
        this.listener = listener;
        receiver.register(this.context);
    }

    /**
     * 释放资源
     */
    public void deInit() {
        receiver.unregister(context);
        this.listener = null;
        this.context = null;
    }
}