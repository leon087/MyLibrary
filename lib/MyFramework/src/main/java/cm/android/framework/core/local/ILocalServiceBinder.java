package cm.android.framework.core.local;

public interface ILocalServiceBinder {

    void initService(ILocalServiceManager serviceManager);

    void create();

    void destroy();

    <T> T getService(String name);

    void addService(String name, Object manager);
}
