// IServiceFetcher.aidl
package cm.android.framework.interfaces;

interface IServiceFetcher {
    IBinder getService(String name);
    void addService(String name,in IBinder service);
    void removeService(String name);
    void clearService();

}
