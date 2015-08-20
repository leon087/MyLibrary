package cm.android.framework.test.cm;

import cm.android.framework.core.ProxyFacoty;

public class ProxyManager implements IProxy {

    private static final ProxyManager proxyManager = new ProxyManager();

    public static ProxyManager getInstance() {
        return proxyManager;
    }

    @Override
    public Object getManager() {
        return ProxyFacoty.<IProxy>getProxy().getManager();
    }

    @Override
    public Object getAccountManager() {
        return ProxyFacoty.<IProxy>getProxy().getAccountManager();
    }

    @Override
    public String getName() {
        return "ProxyManager";
    }
}
