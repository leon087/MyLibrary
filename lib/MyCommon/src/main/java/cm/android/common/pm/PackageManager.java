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

    public static final int INSTALL_SUCCEEDED = 1;

    public static final int INSTALL_FAILED_INVALID_APK = -2;

    public static final int INSTALL_FAILED_INVALID_URI = -3;

    public static final int INSTALL_FAILED_INSUFFICIENT_STORAGE = -4;

    public static final int INSTALL_FAILED_DUPLICATE_PACKAGE = -5;

    public static final int INSTALL_FAILED_NO_SHARED_USER = -6;

    public static final int INSTALL_FAILED_UPDATE_INCOMPATIBLE = -7;

    public static final int INSTALL_FAILED_SHARED_USER_INCOMPATIBLE = -8;

    public static final int INSTALL_FAILED_MISSING_SHARED_LIBRARY = -9;

    public static final int INSTALL_FAILED_REPLACE_COULDNT_DELETE = -10;

    public static final int INSTALL_FAILED_DEXOPT = -11;

    public static final int INSTALL_FAILED_OLDER_SDK = -12;

    public static final int INSTALL_FAILED_CONFLICTING_PROVIDER = -13;

    public static final int INSTALL_FAILED_NEWER_SDK = -14;

    public static final int INSTALL_FAILED_TEST_ONLY = -15;

    public static final int INSTALL_FAILED_CPU_ABI_INCOMPATIBLE = -16;

    public static final int INSTALL_FAILED_MISSING_FEATURE = -17;

    // ------ Errors related to sdcard
    public static final int INSTALL_FAILED_CONTAINER_ERROR = -18;

    public static final int INSTALL_FAILED_INVALID_INSTALL_LOCATION = -19;

    public static final int INSTALL_FAILED_MEDIA_UNAVAILABLE = -20;

    public static final int INSTALL_FAILED_VERIFICATION_TIMEOUT = -21;

    public static final int INSTALL_FAILED_VERIFICATION_FAILURE = -22;

    public static final int INSTALL_FAILED_PACKAGE_CHANGED = -23;

    public static final int INSTALL_FAILED_UID_CHANGED = -24;

    public static final int INSTALL_FAILED_VERSION_DOWNGRADE = -25;

    public static final int INSTALL_FAILED_PERMISSION_MODEL_DOWNGRADE = -26;

    public static final int INSTALL_PARSE_FAILED_NOT_APK = -100;

    public static final int INSTALL_PARSE_FAILED_BAD_MANIFEST = -101;

    public static final int INSTALL_PARSE_FAILED_UNEXPECTED_EXCEPTION = -102;

    public static final int INSTALL_PARSE_FAILED_NO_CERTIFICATES = -103;

    public static final int INSTALL_PARSE_FAILED_INCONSISTENT_CERTIFICATES = -104;

    public static final int INSTALL_PARSE_FAILED_CERTIFICATE_ENCODING = -105;

    public static final int INSTALL_PARSE_FAILED_BAD_PACKAGE_NAME = -106;

    public static final int INSTALL_PARSE_FAILED_BAD_SHARED_USER_ID = -107;

    public static final int INSTALL_PARSE_FAILED_MANIFEST_MALFORMED = -108;

    public static final int INSTALL_PARSE_FAILED_MANIFEST_EMPTY = -109;

    public static final int INSTALL_FAILED_INTERNAL_ERROR = -110;

    public static final int INSTALL_FAILED_USER_RESTRICTED = -111;


    public static final int INSTALL_FAILED_DUPLICATE_PERMISSION = -112;

    public static final int INSTALL_FAILED_NO_MATCHING_ABIS = -113;

    public static final int NO_NATIVE_LIBRARIES = -114;

    public static final int INSTALL_FAILED_ABORTED = -115;

    public static final int DELETE_SUCCEEDED = 1;

    public static final int DELETE_FAILED_INTERNAL_ERROR = -1;

    public static final int DELETE_FAILED_DEVICE_POLICY_MANAGER = -2;

    public static final int DELETE_FAILED_USER_RESTRICTED = -3;

    public static final int DELETE_FAILED_OWNER_BLOCKED = -4;

    public static final int DELETE_FAILED_ABORTED = -5;

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
    public interface PackageInstallObserver {
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
    public interface PackageDeleteObserver {
        /**
         * On package deleted.
         *
         * @param packageName the package name
         * @param returnCode  the return code
         */
        void onPackageDeleted(String packageName, int returnCode);
    }
}
