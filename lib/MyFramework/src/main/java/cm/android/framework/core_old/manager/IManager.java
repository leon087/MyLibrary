package cm.android.framework.core_old.manager;

import android.content.Context;

public interface IManager {

    void init(Context context);

    /**
     * 创建服务
     */
    public void create();

    /**
     * 销毁服务
     */
    public void destroy();
}
