package cm.android.framework.core;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.test.InstrumentationTestCase;

import cm.android.apn.ApnUtil;
import cm.android.util.IntentUtil;

public class CoreReceiverTest extends InstrumentationTestCase {

    public void testRestore() throws Exception {
        Intent intent = new Intent();
        intent.setAction("cm.android.framework.intent.action.CoreReceiver");
        intent.putExtra("processName", "test");
        String action = intent.getAction();
        String processName = intent.getStringExtra("processName");
        boolean result = action.equals("cm.android.framework.intent.action.CoreReceiver")
                && processName.equals("test");
        assertEquals(true, result);
    }
}
