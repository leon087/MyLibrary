package cm.android.global;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import cm.android.util.EnvironmentInfo.SdkUtil;
import cm.android.util.ObjectUtil;

import java.util.Set;

/**
 * Preference包装类，可将数据存储于SharedPreferences中
 */
public class MyPreference {
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    /**
     * 是否是事务模式，在事务模式下，提交的修改都不会写入文件
     */
    private volatile boolean isTransactionMode = false;

    public MyPreference(Context context, String name) {
        settings = context.getSharedPreferences(name, 0);
    }

    /**
     * 开启一个存储事务，开启后修改操作都不会写入文件，必须通过{@link #commitTransaction()}提交修改
     */
    public void beginTransaction() {
        synchronized (this) {
            isTransactionMode = true;
        }
    }

    /**
     * 提交存储事务
     */
    public void commitTransaction() {
        synchronized (this) {
            isTransactionMode = false;
        }
        commitSettings();
    }

    private void commit() {
        if (isTransactionMode) {
            return;
        }

        synchronized (this) {
            if (!isTransactionMode) {
                commitSettings();
            }
        }
    }

    private void commitSettings() {
        synchronized (this) {
            if (editor != null) {
                editor.commit();
                editor = null;
            }
        }
    }

    private void editSettings() {
        if (editor == null) {
            synchronized (this) {
                if (editor == null) {
                    editor = settings.edit();
                }
            }
        }
    }

    private void edit() {
        editSettings();
    }

    /**
     * 读取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public String read(String key, String defaultValue) {
        return settings.getString(key, defaultValue);
    }

    /**
     * 读取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public boolean read(String key, boolean defaultValue) {
        return settings.getBoolean(key, defaultValue);
    }

    /**
     * 读取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public float read(String key, float defaultValue) {
        return settings.getFloat(key, defaultValue);
    }

    /**
     * 读取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public int read(String key, int defaultValue) {
        return settings.getInt(key, defaultValue);
    }

    /**
     * 读取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    public long read(String key, long defaultValue) {
        return settings.getLong(key, defaultValue);
    }

    /**
     * 读取数据
     *
     * @param key
     * @param defaultValue
     * @return
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public Set<String> read(String key, Set<String> defaultValue) {
        if (!SdkUtil.hasHoneycomb()) {
            return ObjectUtil.newHashSet();
        }
        return settings.getStringSet(key, defaultValue);
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    public void write(String key, String value) {
        edit();
        editor.putString(key, value);
        commit();
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    public void write(String key, boolean value) {
        edit();
        editor.putBoolean(key, value);
        commit();
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    public void write(String key, float value) {
        edit();
        editor.putFloat(key, value);
        commit();
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    public void write(String key, int value) {
        edit();
        editor.putInt(key, value);
        commit();
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    public void write(String key, long value) {
        edit();
        editor.putLong(key, value);
        commit();
    }

    /**
     * 写入数据
     *
     * @param key
     * @param value
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void write(String key, Set<String> value) {
        if (!SdkUtil.hasHoneycomb()) {
            commit();
            return;
        }
        edit();
        editor.putStringSet(key, value);
        commit();
    }
}