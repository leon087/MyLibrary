package cm.android.framework.test.cm;

import cm.android.framework.core.ProxyFacoty;

public interface IProxy extends ProxyFacoty.IBaseProxy {

    public Object getManager();

    public Object getAccountManager();
}
