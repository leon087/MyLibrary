// ICoreService.aidl
package cm.android.framework.core;

//import cm.android.framework.core.IServiceManager;
//import cm.android.framework.core.IServiceCreator;

interface IServiceBinder {

//    void initService(IServiceCreator creator);

    void create();

    void destroy();

    IBinder getService(String name);

    void addService(String name, IBinder binder);
}
