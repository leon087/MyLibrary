package cm.android.framework.core;

public interface IApp {

    /**
     * 初始化App运行配置
     */
    AppConfig initConfig();

    IServiceManager initService();
}
