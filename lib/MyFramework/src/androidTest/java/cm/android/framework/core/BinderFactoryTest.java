package cm.android.framework.core;

import android.os.IBinder;
import android.test.InstrumentationTestCase;

public class BinderFactoryTest extends InstrumentationTestCase {

    public void testGetProxy() throws Exception {
        TestManager manager = BinderFactory.getProxy("TestServer", TestManager.class);
        assertEquals(false, manager.get());
    }

    private static class TestManager implements BinderFactory.IBinderProxy {

        private boolean test;

        @Override
        public void bind(IBinder binder) {
            if (binder != null) {
                test = true;
            } else {
                test = false;
            }
        }

        public boolean get() {
            return test;
        }
    }
}
