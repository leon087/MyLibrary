package cm.android.framework.core;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.test.InstrumentationTestCase;

public class CoreServiceTest extends InstrumentationTestCase {

    public void testSetApn() throws Exception {
        Context context = getInstrumentation().getContext();
        boolean result = CoreService.bind(context, mServiceConnection, "123");
        assertEquals(result, true);
    }

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

            if (iBinder == null) {
                return;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
        }
    };
}
