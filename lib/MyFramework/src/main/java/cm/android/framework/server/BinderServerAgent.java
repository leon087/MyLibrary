package cm.android.framework.server;

import android.content.Context;
import android.os.Bundle;

import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicBoolean;

import cm.android.framework.client.core.LogUtil;
import cm.android.framework.component.IBinderServer;
import cm.java.util.Utils;

public final class BinderServerAgent {
    private IBinderServer binderServer;
    private static final IBinderServer defServer = new IBinderServer() {
        @Override
        public void onCreate(Context context) {
            LogUtil.getLogger().error("BinderServerAgent:defServer.onCreate");
        }

        @Override
        public void onDestroy() {
            LogUtil.getLogger().error("BinderServerAgent:defServer.onDestroy");
        }

        @Override
        public boolean isActive(Context context) {
            LogUtil.getLogger().error("BinderServerAgent:defServer.isActive:context = {}", context);
            return false;
        }
    };
    private final AtomicBoolean create = new AtomicBoolean(false);

    private String serverName;
    private Bundle bundle = new Bundle();

    public void attach(String serverName) {
        this.serverName = serverName;
    }

    public IBinderServer getServer() {
        if (binderServer == null) {
            binderServer = createBinderServer(serverName);
        }

        if (binderServer == null) {
            return defServer;
        } else {
            return binderServer;
        }
    }

    private IBinderServer createBinderServer(String serverName) {
        LogUtil.getLogger().info("serverName = {},binderServer = {}", serverName, binderServer);

        if (Utils.isEmpty(serverName)) {
            return null;
        }

        try {
            Class klass = Class.forName(serverName);
            Constructor constructor = klass.getDeclaredConstructor();
            constructor.setAccessible(true);
            IBinderServer server = (IBinderServer) constructor.newInstance();
            LogUtil.getLogger().info("constructor:binderServer = {}", server);
            return server;
        } catch (Exception e) {
            LogUtil.getLogger().error(e.getMessage());
            return null;
        }
    }

    public void create(Context context) {
        LogUtil.getLogger().info("create:create = {},binderServer = {}", create.get(), binderServer);
        if (create.get()) {
            return;
        }

        try {
            getServer().onCreate(context);
            LogUtil.getLogger().info("getServer().onCreate():success");
        } finally {
            create.set(true);
        }
    }

    public void destroy() {
        LogUtil.getLogger().info("destroy:create = {},binderServer = {}", create.get(), binderServer);
        if (!create.get()) {
            return;
        }

        try {
            getServer().onDestroy();
            LogUtil.getLogger().info("getServer().onDestroy():success");
        } finally {
            create.set(false);
        }
    }

    public boolean isActive(Context context) {
        boolean createBoolean = create.get();
        boolean isActive = getServer().isActive(context);
        LogUtil.getLogger().info("createBoolean = {},isActive = {}", createBoolean, isActive);
        return createBoolean && isActive;
    }

    public Bundle getBundle(String key) {
        return bundle.getBundle(key);
    }

    public void putBundle(String key, Bundle bundle) {
        this.bundle.putBundle(key, bundle);
    }

    public void restore(Context context) {
        if (getServer().isActive(context)) {
            create(context);
        }
    }
}
