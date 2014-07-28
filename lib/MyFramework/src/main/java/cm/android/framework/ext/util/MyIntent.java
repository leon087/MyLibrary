package cm.android.framework.ext.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cm.android.framework.ext.global.CommonTag;
import cm.android.global.MyParcelable;
import cm.android.util.ObjectUtil;

/**
 * 封装了一些Intent操作
 */
public class MyIntent {

    /**
     * 跳转Activity，有返回值，参考{@link Activity#startActivityForResult}
     *
     * @param <T>
     * @param activity
     * @param clazz
     * @param requestCode If >= 0, this code will be returned in onActivityResult() when
     *                    the activity exits.
     */
    public static <T> void startActivityForResult(Activity activity,
                                                  Class<?> clazz, T data, int requestCode) {
        Intent intent = getActivityIntent(activity, clazz, data, false);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 跳转Activity
     *
     * @param <T>
     * @param context
     * @param
     */
    public static <T> void startActivity(Context context, Class<?> clazz, T data) {
        startActivity(context, clazz, data, false);
    }

    /**
     * 跳转Activity
     *
     * @param <T>
     * @param context
     * @param clazz     跳转activity的类名
     * @param isNewtask true: 添加{@link Intent.FLAG_ACTIVITY_NEW_TASK}标记
     */
    public static <T> void startActivity(Context context, Class<?> clazz,
                                         T data, boolean isNewtask) {
        Intent intent = getActivityIntent(context, clazz, data, isNewtask);
        context.startActivity(intent);
    }

    public static <T> Intent getActivityIntent(Context context, Class<?> clazz,
                                               T data, boolean isNewtask) {
        Intent intent = new Intent(context, clazz);
        intent = setData(intent, data);
        if (isNewtask) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        // intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    /**
     * startActivity()跳转后获取跳转时传递数据
     *
     * @param <T>
     * @param intent
     * @return
     */
    public static <T> T getData(Intent intent) {
        MyParcelable<T> parcelable = intent
                .getParcelableExtra(CommonTag.INTENT_PAGE);
        if (parcelable != null) {
            return parcelable.getValue();
        }
        return null;
    }

    /**
     * 获取Bundle中数据
     *
     * @param <T>
     * @param bunlde
     * @return
     */
    public static <T> T getData(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        MyParcelable<T> parcelable = bundle
                .getParcelable(CommonTag.INTENT_PAGE);
        if (parcelable != null) {
            return parcelable.getValue();
        }
        return null;
    }

    /**
     * 添加数据到Intent中
     *
     * @param <T>
     * @param intent
     * @param data
     * @return
     */
    public static <T> Intent setData(Intent intent, T data) {
        if (data != null) {
            MyParcelable<T> parcelable = ObjectUtil.newParcelable();
            parcelable.setValue(data);
            intent.putExtra(CommonTag.INTENT_PAGE, parcelable);
        }
        return intent;
    }

    /**
     * 添加数据到Bundle中
     *
     * @param <T>
     * @param bundle
     * @param data
     * @return
     */
    public static <T> Bundle setData(Bundle bundle, T data) {
        if (data != null) {
            MyParcelable<T> parcelable = ObjectUtil.newParcelable();
            parcelable.setValue(data);
            bundle.putParcelable(CommonTag.INTENT_PAGE, parcelable);
        }
        return bundle;
    }

    /**
     * 发送带数据的广播
     *
     * @param <T>
     * @param context
     * @param action
     */
    public static <T> void sendBroadcast(Context context, String action, T data) {
        Intent intent = new Intent(action);
        setData(intent, data);
        context.sendBroadcast(intent);
    }
}
