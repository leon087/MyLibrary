package cm.android.framework.ext.global;

import cm.android.callback.Observable;
import cm.android.util.ObjectUtil;

import java.util.Map;

/**
 * 帐号信息
 */
public class AccountData extends Observable {
    /**
     * 是否登录
     */
    public volatile boolean accountLogin = false;
    private Map<String, Object> mData = ObjectUtil.newHashMap();

    /**
     * 初始化资源
     */
    public AccountData() {
        setChanged();
    }

    /**
     * 释放资源
     */
    public void release() {
        clearChanged();
        deleteObservers();
        mData.clear();
        accountLogin = false;
    }

    /**
     * 根据key值获取全局数据
     *
     * @param key
     * @return
     */
    public Object getData(String key) {
        return mData.get(key);
    }

    /**
     * 保存全局数据
     *
     * @param key
     * @param value
     * @return
     */
    public Object putData(String key, Object value) {
        return mData.put(key, value);
    }

    @Override
    public void notifyObservers(Object data) {
        setChanged();
        super.notifyObservers(data);
    }

    @Override
    public void notifyObservers() {
        setChanged();
        super.notifyObservers();
    }
}
