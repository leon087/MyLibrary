// ICoreService.aidl
package cm.android.framework.core;

import cm.android.framework.core.IServiceManager;

interface IServiceBinder {

    void initService(IServiceManager serviceManager);

    void create();

    void destroy();

    IBinder getService(String name);

    void addService(String name, IBinder binder);
}
