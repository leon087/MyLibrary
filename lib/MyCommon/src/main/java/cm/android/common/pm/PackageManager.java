package cm.android.common.pm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.Context;
import android.content.pm.IPackageDeleteObserver;
import android.net.Uri;
import android.os.RemoteException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import cm.java.util.ReflectUtil;

/**
 * The type Zte package manager.
 */
public class PackageManager {
    private static Logger logger = LoggerFactory.getLogger("pm");

    private static final String IPACKAGE_INSTALL_OBSERVER = "android.content.pm.IPackageInstallObserver";

    private static final String IPACKAGE_DELETE_OBSERVER = "android.content.pm.IPackageDeleteObserver";

    /**
     * Flag parameter for {@link #android.content.pm.PackageManager.installPackage} to indicate
     * that
     * you want to replace an already
     * installed package, if one exists.
     */
    public static final int INSTALL_REPLACE_EXISTING = 0x00000002;

    private static volatile PackageManager INSTANCE;

    public static PackageManager getInstance() {
        if (null == INSTANCE) {
            synchronized (PackageManager.class) {
                if (null == INSTANCE) {
                    INSTANCE = new PackageManager();
                }
            }
        }
        return INSTANCE;
    }

    private PackageManager() {
    }

    /**
     * Install.
     *
     * @param uri      the uri
     * @param observer the observer
     */
    public void installPackage(Context context, Uri uri, final PackageInstallObserver observer, int flags) {
        Class clazz = null;
        try {
            clazz = Class.forName(IPACKAGE_INSTALL_OBSERVER);
        } catch (ClassNotFoundException e) {
            logger.error("error = {}", e.getMessage());
            return;
        }

        Method installPackage = ReflectUtil.getMethod(context.getPackageManager().getClass(), "installPackage", new Class[]{Uri.class, clazz, int.class, String.class});
        Object observerProxy = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                logger.info("method = {}", method);
                if ("installPackage".equals(method.getName())) {
                    logger.info("installPackage method = {}, packageName = {}, returnCode = {}", method.getName(), args[0], args[1]);
                    if (null != observer) {
                        observer.onPackageInstalled(String.valueOf(args[0]), (int) args[1]);
                    }
                }
                return null;
            }
        });

        ReflectUtil.doMethod(context.getPackageManager(), installPackage, new Object[]{uri, observerProxy, flags, context.getPackageName()});
    }

    /**
     * Uninstall.
     *
     * @param packageName the package name
     * @param observer    the observer
     */
    public void deletePackage(Context context, String packageName, final PackageDeleteObserver observer, int flags) {
        Class clazz = null;
        try {
            clazz = Class.forName(IPACKAGE_DELETE_OBSERVER);
        } catch (ClassNotFoundException e) {
            logger.error("error = {}", e.getMessage());
            return;
        }

        Method deletePackage = ReflectUtil.getMethod(context.getPackageManager().getClass(), "deletePackage", new Class[]{String.class, clazz, int.class});
        Object observerProxy = Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                logger.info("method = {}", method);
                if ("asBinder".equals(method.getName())) {
                    return new IPackageDeleteObserver.Stub() {

                        @Override
                        public void packageDeleted(String packageName, int returnCode) throws RemoteException {
                            logger.info("packageDeleted pacakageName = {}, returnCode = {}", packageName, returnCode);
                            if (null != observer) {
                                observer.onPackageDeleted(packageName, returnCode);
                            }
                        }
                    };
                }

                return null;
            }
        });

        ReflectUtil.doMethod(context.getPackageManager(), deletePackage, new Object[]{packageName, observerProxy, flags});
    }

    /**
     * The interface Package install observer.
     */
    interface PackageInstallObserver {
        /**
         * On package installed.
         *
         * @param packageName the package name
         * @param returnCode  the return code
         */
        void onPackageInstalled(String packageName, int returnCode);
    }

    /**
     * The interface Package delete observer.
     */
    interface PackageDeleteObserver {
        /**
         * On package deleted.
         *
         * @param packageName the package name
         * @param returnCode  the return code
         */
        void onPackageDeleted(String packageName, int returnCode);
    }
}
