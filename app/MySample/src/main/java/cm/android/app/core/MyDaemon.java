//package cm.android.app.core;
//
//
//import android.content.Context;
//
//import cm.android.framework.server.daemon.DaemonService;
//
//public class MyDaemon {
//
//    private static MyDaemon instance;
//
//    private DaemonClient daemonClient;
//
//    public static MyDaemon getInstance() {
//        if (instance == null) {
//            synchronized (MyDaemon.class) {
//                if (instance == null) {
//                    instance = new MyDaemon();
//                }
//            }
//        }
//        return instance;
//    }
//
//    public void attachDaemon(Context context) {
//        daemonClient = new DaemonClient(createDaemonConfigurations(context));
//        daemonClient.onAttachBaseContext(context);
//    }
//
//    private DaemonConfigurations createDaemonConfigurations(Context context) {
//        String persistentProcess = context.getPackageName() + ":framework";
//        String assitProcess = context.getPackageName() + ":daemon";
//
//        DaemonConfigurations.DaemonConfiguration configuration1
//                = new DaemonConfigurations.DaemonConfiguration(
//                persistentProcess,
//                DaemonService.class.getCanonicalName(),
//                DaemonReceiver.class.getCanonicalName());
//        DaemonConfigurations.DaemonConfiguration configuration2
//                = new DaemonConfigurations.DaemonConfiguration(
//                assitProcess,
//                DaemonAssitService.class.getCanonicalName(),
//                DaemonAssitReceiver.class.getCanonicalName());
//        DaemonConfigurations.DaemonListener listener = new MyDaemonListener();
//        return new DaemonConfigurations(configuration1, configuration2, listener);
//    }
//
//
//    class MyDaemonListener implements DaemonConfigurations.DaemonListener {
//
//        @Override
//        public void onPersistentStart(Context context) {
//        }
//
//        @Override
//        public void onDaemonAssistantStart(Context context) {
//        }
//
//        @Override
//        public void onWatchDaemonDaed() {
//        }
//    }
//}
