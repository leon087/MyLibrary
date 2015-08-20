package cm.android.framework.core;


import android.test.InstrumentationTestCase;

public class ProxyFacotyTest extends InstrumentationTestCase {

    public void testCreate() throws Exception {
        ProxyFacoty.register(Test.class);
        Object proxy = ProxyFacoty.create();
        boolean result = proxy == null;
        assertEquals(result, true);
    }

    class Test implements ProxyFacoty.IBaseProxy {

        @Override
        public String getName() {
            return Test.class.getName();
        }
    }
}
