package cm.android.framework.core.local;

public final class LocalServiceManager {

    public static void addService(String name, Object manager) {
        LocalBaseApp.getApp().addService(name, manager);
    }

    public static <T> T getService(String name) {
        return LocalBaseApp.getApp().getService(name);
    }

    public static boolean isBindService() {
        return LocalBaseApp.getApp().isBindService();
    }
}
