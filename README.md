
简介
-----
- **MyFramework**：基础框架，支持后台保活、多进程绑定、支持跨进程共享Binder
- MyUtil：util集合
- MyCommon：功能模块，包含了数据库、缓存、静默与非静默安装卸载等


MyFramework
-----

初始化与关闭：
```java
public class MainApp extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        Framework.get().config(new Config.Builder()
                .authorities(base.getPackageName() + "." + ServerProvider.AUTHORITIES)
                .serverProcess(base.getApplicationInfo().processName + ":framework")
                .build());
        Framework.get().startup(base, MyBinderServer.class);
    }
}
```

```java
/**
*  运行在framework进程
*/
public class MyBinderServer extends BaseBinderServer {
    private static final Logger logger = LoggerFactory.getLogger("MyBinderServer");

    private TestManagerServer testManager;
    private Context context;

    private TimerServer timerServer;

    @Override
    protected void create(Context context) {
        this.context = context;
        timerServer = new TimerServer();
        timerServer.start(context);

        Framework.addService(TestContext.TIMER_TASK_SERVER, timerServer);
    }

    @Override
    protected void destroy() {
        ThreadUtil.newCachedThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                Bundle bundle = Framework.get().getBundle("ggg");
                while (bundle != null && !bundle.getBoolean("exit")) {
                    logger.error("hhh bundle.getBoolean(\"exit\") = " + bundle.getBoolean("exit"));
                    ThreadUtil.sleep(20);
                    bundle = Framework.get().getBundle("ggg");
                }

                logger.error("hhh destroy");
                Framework.clearService();
                timerServer.stop();
                MyBinderServer.this.context = null;
            }
        });
    }

    @Override
    protected void startService() {
        MainService.start(context);
    }

    @Override
    protected void stopService() {
        MainService.stop(context);
    }
}
```

```java
public class App {
    public void init() {
        //初始化app
        Framework.get().start();
    }

    public void deInit() {
        //退出app
        Framework.get().stop();
    }

    public static TimerManager getTimerManager() {
        TimerManager test = Framework.getBinderProxy(TestContext.TIMER_TASK_SERVER, TimerManager.class);
        return test;
    }
}
```

License
---
Apache 2.0

