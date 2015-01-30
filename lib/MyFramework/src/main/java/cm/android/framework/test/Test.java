package cm.android.framework.test;

import cm.android.framework.core.ProxyFacoty;
import cm.android.framework.test.cm.CmInterface;
import cm.android.framework.test.cm.ProxyManager;

public class Test {

    public static void main() {
        ProxyFacoty.register(CmInterface.class);
        ProxyManager.getInstance().getManager();
        ProxyManager.getInstance().getAccountManager();
    }
}
