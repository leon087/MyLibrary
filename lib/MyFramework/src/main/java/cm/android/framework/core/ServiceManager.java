package cm.android.framework.core;

public final class ServiceManager {

    public static void addService(String name, IManager manager) {
        BaseApp.getApp().addService(name, manager);
    }

    public static IManager getService(String name) {
        return BaseApp.getApp().getService(name);
    }

    public static boolean isBindService() {
        return BaseApp.getApp().isBindService();
    }
}
