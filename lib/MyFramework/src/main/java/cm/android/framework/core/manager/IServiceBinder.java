package cm.android.framework.core.manager;

public interface IServiceBinder {

    void initService(IServiceManager serviceManager);

    void create();

    void destroy();

    <T> T getService(String name);

    void addService(String name, Object manager);
}
