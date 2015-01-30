package cm.android.framework.test.cm;

import cm.android.framework.core.ProxyFacoty;

public class ProxyManager implements IProxy {

    private static ProxyManager mProxyManager = new ProxyManager();

    public static ProxyManager getInstance() {
        return mProxyManager;
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
