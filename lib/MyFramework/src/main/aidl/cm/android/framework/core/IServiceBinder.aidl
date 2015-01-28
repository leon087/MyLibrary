// ICoreService.aidl
package cm.android.framework.core;

import cm.android.framework.core.IServiceManager;
import cm.android.framework.core.IManager;

interface IServiceBinder {

    void initService(IServiceManager serviceManager);

    void create();

    void destroy();

    IManager getService(String name);

    void addService(String name, IManager manager);
}
