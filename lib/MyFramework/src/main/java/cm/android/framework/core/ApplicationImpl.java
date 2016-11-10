package cm.android.framework.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.applications.AppUtil;
import cm.android.framework.server.daemon.DaemonService;
import cm.android.util.IntentUtil;
import cm.android.util.SystemUtil;
import cm.java.util.IoUtil;

import static android.content.Context.MODE_PRIVATE;

@Deprecated
public final class ApplicationImpl {

    private static final Logger logger = LoggerFactory.getLogger("framework");

    private final AtomicBoolean startAtomic = new AtomicBoolean(false);

    private Context appContext;

    private final ServiceBinderProxy serviceBidnerProxy = new ServiceBinderProxy();

    //    private ServiceManager.InitListener initListener;
    private volatile WeakReference<ServiceManager.InitListener> initListener;

    final synchronized boolean isStarted() {
//        return startAtomic.get();
        return serviceBidnerProxy.isInit();
    }

    void appInit(Context context, ServiceManager.AppConfig appConfig,
                 Class<? extends IServiceManager> serviceClass) {
        String processName = SystemUtil.getCurProcessName();
        if (appContext != null) {
            logger.error("processName = {},old.appContext = {},new.context = {}", processName, appContext, context);
            throw new IllegalArgumentException("appContext = " + appContext);
        }

        if (serviceClass == null) {
            throw new IllegalArgumentException("serviceClass = null");
        }
        String serviceName = serviceClass.getName();

        appContext = context.getApplicationContext();
        appConfig.init(appContext);
        CoreService.bind(appContext, mServiceConnection, serviceName);
//        Bundle response = new ProviderCall.Builder(context, SERVICE_CP_AUTH).methodName("@").call();

        PackageInfo packageInfo = AppUtil.getPackageInfo(
                appContext.getPackageManager(), appContext.getPackageName(),
                PackageManager.GET_ACTIVITIES);
        if (packageInfo == null) {
            logger.error("packageInfo = null,getPackageName() = {},processName = {}", appContext.getPackageName(), SystemUtil.getCurProcessName());
        } else {
            logger.info("versionCode = {},versionName = {},processName = {}", packageInfo.versionCode, packageInfo.versionName, processName);
        }
    }

    final void handleAction(String action, Bundle bundle) {
        CoreService.start(appContext, action, bundle);
    }

    final void start(ServiceManager.InitListener initListener) {
        logger.info("start");
        if (startAtomic.get()) {
            logger.error("start:isStarted = true");
            return;
        }

        StateHolder.writeState(appContext, true);

        synchronized (this) {
            this.initListener = new WeakReference<>(initListener);
        }

        if (isSystemReady()) {
            startInternal();
        } else {
            logger.error("start:isSystemReady = false");
        }
    }

    private synchronized void startInternal() {
        if (!startAtomic.get()) {
            DaemonService.start(appContext);
        } else {
            logger.error("startInternal:startAtomic = " + startAtomic.get());
        }

        serviceBidnerProxy.create();
        notifyInitSucceed();
        startAtomic.set(true);
    }

    private synchronized void stopInternal() {
        synchronized (this) {
            this.initListener = null;
        }
        serviceBidnerProxy.destroy();

        if (startAtomic.get()) {
            DaemonService.stop(appContext);
        }
        startAtomic.set(false);
    }

    final void stop() {
        logger.info("stop");
        StateHolder.writeState(appContext, false);

        stopInternal();
    }

    private void systemReady() {
        //状态恢复
        if (StateHolder.isStateInit(appContext)) {
            startInternal();
        }

        notifyBindSucceed(appContext);
    }

    public final static void notifyBindSucceed(Context context) {
        Intent intent = new Intent(ServiceManager.ACTION_BIND_SUCCEED);
        //绑定成功发送本地广播
        IntentUtil.sendBroadcastLocal(context, intent);
    }

    private void systemFailed() {
        if (StateHolder.isStateInit(appContext)) {
            stopInternal();
        }
    }

    //TODO ggg 采用Provider.call()查询iBinder
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            logger.info("onServiceConnected:iBinder = {},processName = {}", iBinder, SystemUtil.getCurProcessName());

            if (iBinder == null) {
                logger.error("iBinder = null");
                return;
            }

            //TODO ggg
            serviceBidnerProxy.bindServiceBinder(iBinder);

            systemReady();
            logger.info("systemReady:isBindService = {}", serviceBidnerProxy.isBindService());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            logger.error("onServiceDisconnected:processName = {}", SystemUtil.getCurProcessName());
            serviceBidnerProxy.bindServiceBinder(null);

            systemFailed();
        }
    };

    final IBinder getService(String name) {
        return serviceBidnerProxy.getService(name);
    }

    final void addService(String name, IBinder binder) {
        serviceBidnerProxy.addService(name, binder);
    }

//    final boolean isBindService() {
//        return serviceBidnerProxy.isBindService();
//    }

    final boolean isSystemReady() {
        return serviceBidnerProxy.isBindService();
    }

    private void notifyInitSucceed() {
        synchronized (this) {
            if (initListener == null) {
                return;
            }

            ServiceManager.InitListener listener = initListener.get();
            if (listener == null) {
                return;
            }

            listener.initSucceed();
            this.initListener = null;
        }
    }

    public static class StateHolder {

        private static final String STATE_FILE_NAME = "framework_app_status";

        private static final String TAG_STATE = "state";

        private static final String ID = "_id";

        private static final String DB_NAME = "framework.db";

        public static boolean isStateInit(Context context) {
            return readState(context);
        }

//        public static class OpenHelper extends SQLiteOpenHelper {
//
//            public static final int VERSION = 1;
//
//            public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
//                super(context, name, factory, VERSION);
//            }
//
//            @Override
//            public void onCreate(SQLiteDatabase db) {
//                String CREATE_TABLE = "create table if not exists " + STATE_FILE_NAME
//                        + "(" + ID + " integer primary key autoincrement,"
//                        + TAG_STATE + " text)";
//                db.execSQL(CREATE_TABLE);
//            }
//
//            @Override
//            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//            }
//        }

        public static void writeState(Context context, boolean state) {
            logger.info("StateHolder:writeState:state = " + state);

            Properties properties = new Properties();
            properties.setProperty(TAG_STATE, String.valueOf(state));

            OutputStream os = null;
            try {
                os = context.openFileOutput(STATE_FILE_NAME, MODE_PRIVATE);
                properties.store(os, "writeState:state = " + state);
            } catch (IOException e) {
                logger.error(e.getMessage());
            } finally {
                IoUtil.closeQuietly(os);
            }

//            OpenHelper helper = new OpenHelper(context, DB_NAME, null);
//            SQLiteDatabase dbw = helper.getWritableDatabase();
//            SQLiteDatabase dbr = helper.getReadableDatabase();
//            ContentValues cv = new ContentValues();
//            cv.put(TAG_STATE, String.valueOf(state));
//
////            String sql = new StringBuilder()
////                    .append("select * from ")
////                    .append(STATE_FILE_NAME)
////                    .append(" where ")
////                    .append(TAG_STATE)
////                    .append(" =? ")
////                    .toString();
//            Cursor cursor = null;
//            try {
//                cursor = dbr.query(STATE_FILE_NAME, null, null, null, null, null, ID + " asc");
//                if (null == cursor || !cursor.moveToFirst()) {
//                    //查询不到表明未插入
//                    dbw.insert(STATE_FILE_NAME, "false", cv);
//                } else {
//                    //已插入，更新值
//                    String whereClause = " " + ID + " = ? ";
//                    String[] whereArgs = {String.valueOf(1)};
//                    dbw.update(STATE_FILE_NAME, cv, whereClause, whereArgs);
//                }
//            } catch (Exception e) {
//                logger.error(e.getMessage());
//            } finally {
//                IoUtil.closeQuietly(cursor);
//            }
////            Cursor c = db.rawQuery("select * from "+STATE_FILE_NAME+" where username=? and password = ?",
////                    new Stirng[]{"用户名","密码"});

        }

        private static boolean readState(Context context) {
//            OpenHelper helper = new OpenHelper(context, DB_NAME, null);
//            SQLiteDatabase dbr = helper.getReadableDatabase();
//            Cursor cursor = null;
//            try {
//                cursor = dbr.query(STATE_FILE_NAME, null, null, null, null, null, ID + " asc");
//                if (null == cursor || !cursor.moveToFirst()) {
//                    //查询不到表明未插入
//                    logger.info("StateHolder:writeState:state = false,cursor = null");
//                    return false;
//                }
//
//                boolean state = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(TAG_STATE)));
//                logger.info("StateHolder:writeState:state = {}", state);
//                return state;
//            } catch (Exception e) {
//                logger.error("StateHolder:writeState:state = false,error = {}", e.getMessage());
//                return false;
//            } finally {
//                IoUtil.closeQuietly(cursor);
//            }

            FileInputStream fis = null;
            try {
                fis = context.openFileInput(STATE_FILE_NAME);
                Properties properties = new Properties();
                properties.load(fis);

                boolean state = Boolean.valueOf(properties.getProperty(TAG_STATE, String.valueOf(false)));
                logger.info("StateHolder:readState:state = " + state);
                return state;
            } catch (IOException e) {
                logger.info("StateHolder:readState:state = false:" + e.getMessage());
                return false;
            } finally {
                IoUtil.closeQuietly(fis);
            }
        }
    }

    private static class ServiceBinderProxy extends IServiceBinder.Stub {

        private static final Logger logger = LoggerFactory.getLogger("framework");

        private IServiceBinder serviceBinder;

        ServiceBinderProxy() {
        }

        void bindServiceBinder(IBinder iBinder) {
            this.serviceBinder = IServiceBinder.Stub.asInterface(iBinder);
        }

        boolean isBindService() {
            if (serviceBinder == null) {
                logger.error("isBindService:serviceBinder = null");
                return false;
            }
            return serviceBinder.asBinder().isBinderAlive();
        }

        @Override
        public void create() {
            if (!isBindService()) {
                return;
            }

            try {
                serviceBinder.create();
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
            }
        }

        @Override
        public void destroy() {
            if (!isBindService()) {
                return;
            }

            try {
                serviceBinder.destroy();
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }

        @Override
        public IBinder getService(String name) {
            if (!isBindService()) {
                logger.error("getService:name = " + name);
                return null;
            }

            try {
                return serviceBinder.getService(name);
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
                return null;
            }
        }

        @Override
        public void addService(String name, IBinder binder) {
            if (!isBindService()) {
                logger.error("addService:name = {},binder = {}", name, binder);
                return;
            }

            try {
                serviceBinder.addService(name, binder);
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
            }
        }

        @Override
        public boolean isInit() {
            if (!isBindService()) {
                logger.error("isInit:isBindService = false");
                return false;
            }

            try {
                return serviceBinder.isInit();
            } catch (RemoteException e) {
                logger.error(e.getMessage(), e);
                return false;
            }
        }
    }
}
