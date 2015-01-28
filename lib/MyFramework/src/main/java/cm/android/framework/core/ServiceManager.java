package cm.android.framework.core;

public final class ServiceManager {

    public static void addService(String name, Object manager) {
        BaseApp.getApp().addService(name, manager);
    }

    public static <T> T getService(String name) {
        return BaseApp.getApp().getService(name);
    }

    public static boolean isBindService() {
        return BaseApp.getApp().isBindService();
    }
}
