package cm.android.framework.core;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.test.InstrumentationTestCase;
import android.test.UiThreadTest;

import cm.java.thread.ThreadUtil;


public class ApplicationImplTest extends InstrumentationTestCase {

//    BroadcastReceiver receiver;

//    @UiThreadTest
//    public void testNotifyBindSucceed() throws Exception {
//
//        receiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                String action = intent.getAction();
//                assertEquals(true, false);
////                assertEquals(ServiceManager.ACTION_BIND_SUCCEED, action);
//
//                ApplicationImplTest.this.notifyAll();
//            }
//        };
//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ServiceManager.ACTION_BIND_SUCCEED);
//        getInstrumentation().getContext().registerReceiver(receiver, filter);
//        getInstrumentation().getContext().sendBroadcast(intent);

//        getInstrumentation().getContext().registerReceiver();

//        ApplicationImpl.notifyBindSucceed(getInstrumentation().getContext());
//        un

//        ApplicationImplTest.this.wait();

//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ServiceManager.ACTION_BIND_SUCCEED);
//        getInstrumentation().getContext().registerReceiver(receiver, filter);
//
//        Intent intent = new Intent();
//        intent.setAction(ServiceManager.ACTION_BIND_SUCCEED);
//        getInstrumentation().getContext().sendBroadcast(intent);
//
//    }
}
