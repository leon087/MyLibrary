package cm.android.framework.core.global;

import java.util.Map;

import cm.android.util.AndroidUtils;

/**
 * 用户保存全局数据
 */
public class GlobalData {

    private Map<String, Object> mData = AndroidUtils.newMap();

    private GlobalData() {
    }

    private static final class Singleton {

        private static final GlobalData INSTANCE = new GlobalData();
    }

    public static GlobalData getInstance() {
        return Singleton.INSTANCE;
    }

    public void reset() {
        mData.clear();
    }

    /**
     * 获取全局数据
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(String key) {
        return (T) mData.get(key);
    }

    /**
     * 保存全局数据
     */
    public <T> Object putData(String key, T value) {
        return mData.put(key, value);
    }
}
