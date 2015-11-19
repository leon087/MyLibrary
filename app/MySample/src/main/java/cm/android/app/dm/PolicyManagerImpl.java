package cm.android.app.dm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;

import cm.android.util.EnvironmentUtil;

public final class PolicyManagerImpl {

    private static final Logger logger = LoggerFactory.getLogger("DeviceAdmin");

    private ComponentName componentName;

    private DevicePolicyManager policyManager;

    private Context context;

    private IDeviceAdminActive iDeviceAdminActive;

    private static PolicyManagerImpl instance;

    private PolicyManagerImpl() {
    }

    public static PolicyManagerImpl getInstance(Context context) {
        if (instance == null) {
            synchronized (PolicyManagerImpl.class) {
                if (instance == null) {
                    instance = new PolicyManagerImpl();
                    instance.init(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context.getApplicationContext();
        if (policyManager == null) {
            //获取设备管理服务
            policyManager = (DevicePolicyManager) context
                    .getSystemService(Context.DEVICE_POLICY_SERVICE);
            componentName = DefaultDeviceAdminActive.getAdminComponent();
        }
    }

    public void deInit() {
        policyManager = null;
        componentName = null;
        iDeviceAdminActive = null;

        instance = null;
        this.context = null;
    }

    public void checkQuality() {
        checkActive();

        if (instance.getPasswordQuality() != DevicePolicyManager.PASSWORD_QUALITY_COMPLEX) {
            instance.setPasswordQuality(DevicePolicyManager.PASSWORD_QUALITY_COMPLEX);
        }
    }

    public void setDeviceAdminActive(IDeviceAdminActive iDeviceAdminActive) {
        this.iDeviceAdminActive = iDeviceAdminActive;
    }

    public boolean checkActive() {
        boolean active = isAdminActive();
        if (!active) {
            AdminDaemonService.start(this.context);
        }
        return active;
    }

    public void unActive() {
        if (isAdminActive()) {
            policyManager.removeActiveAdmin(componentName);
        }
    }

    public boolean isAdminActive() {
        return policyManager.isAdminActive(componentName);
    }

    void onEnabled() {
        checkQuality();
    }

    void onDisabled() {
//        擦除数据
//        WipeDomainService
//                .wipeDomain(MyManager.getAppContext(), Protocol.EventProtocol.DOMAIN_DELETE_ADMIN);
    }

    public static interface IDeviceAdminActive {

        void active();
    }

    public void removeActiveAdmin() {
        if (!checkActive()) {
            return;
        }
        policyManager.removeActiveAdmin(componentName);
    }

    public boolean hasGrantedPolicy(int usesPolicy) {
        if (!checkActive()) {
            return false;
        }
        return policyManager.hasGrantedPolicy(componentName, usesPolicy);
    }

    public void setPasswordQuality(int quality) {
        if (!checkActive()) {
            return;
        }
        policyManager.setPasswordQuality(componentName, quality);
    }

    public int getPasswordQuality() {
        if (!checkActive()) {
            return DevicePolicyManager.PASSWORD_QUALITY_COMPLEX;
        }
        return policyManager.getPasswordQuality(componentName);
    }

//    void setPasswordMinimumLength(int length) {
//        profile.setPasswordMinimumLength(length);
//
//        if (!checkActive()) {
//            return;
//        }
//        policyManager.setPasswordMinimumLength(componentName, length);
//    }

    public int getPasswordMinimumLength() {
        if (!checkActive()) {
            return 0;
        }
        return policyManager.getPasswordMinimumLength(componentName);
    }

//    public void setPasswordMinimumUpperCase(int length) {
//        profile.setPasswordMinimumUpperCase(length);
//
//        if (!checkActive()) {
//            return;
//        }
//        policyManager.setPasswordMinimumUpperCase(componentName, length);
//    }

    public int getPasswordMinimumUpperCase() {
        if (!checkActive()) {
            return 0;
        }
        return policyManager.getPasswordMinimumUpperCase(componentName);
    }

//    public void setPasswordMinimumLowerCase(int length) {
//        profile.setPasswordMinimumLowerCase(length);
//
//        if (!checkActive()) {
//            return;
//        }
//        policyManager.setPasswordMinimumLowerCase(componentName, length);
//    }

    public int getPasswordMinimumLowerCase() {
        if (!checkActive()) {
            return 0;
        }
        return policyManager.getPasswordMinimumLowerCase(componentName);
    }

//    public void setPasswordMinimumLetters(int length) {
//        profile.setPasswordMinimumLetters(length);
//
//        if (!checkActive()) {
//            return;
//        }
//        policyManager.setPasswordMinimumLetters(componentName, length);
//    }

    public int getPasswordMinimumLetters() {
        if (!checkActive()) {
            return 0;
        }
        return policyManager.getPasswordMinimumLetters(componentName);
    }

//    public void setPasswordMinimumNumeric(int length) {
//        profile.setPasswordMinimumNumeric(length);
//
//        if (!checkActive()) {
//            return;
//        }
//        policyManager.setPasswordMinimumNumeric(componentName, length);
//    }

    public int getPasswordMinimumNumeric() {
        if (!checkActive()) {
            return 0;
        }
        return policyManager.getPasswordMinimumNumeric(componentName);
    }
//
//    void setPasswordMinimumSymbols(int length) {
//        profile.setPasswordMinimumSymbols(length);
//
//        if (!checkActive()) {
//            return;
//        }
//        policyManager.setPasswordMinimumSymbols(componentName, length);
//    }setMaximumFailedPasswordsForWipe

    public int getPasswordMinimumSymbols() {
        if (!checkActive()) {
            return 0;
        }
        return policyManager.getPasswordMinimumSymbols(componentName);
    }

//    void setPasswordMinimumNonLetter(int length) {
//        profile.setPasswordMinimumNonLetter(length);
//
//        if (!checkActive()) {
//            return;
//        }
//        policyManager.setPasswordMinimumNonLetter(componentName, length);
//    }

    public int getPasswordMinimumNonLetter() {
        if (!checkActive()) {
            return 0;
        }
        return policyManager.getPasswordMinimumNonLetter(componentName);
    }

//    void setPasswordHistoryLength(int length) {
//        profile.setPasswordHistoryLength(length);
//
//        if (!checkActive()) {
//            return;
//        }
//        policyManager.setPasswordHistoryLength(componentName, length);
//    }

//    void setPasswordExpirationTimeout(long timeout) {
//        profile.setPasswordExpirationTimeout(timeout);
//
//        if (!checkActive()) {
//            return;
//        }
//        policyManager.setPasswordExpirationTimeout(componentName, timeout);
//    }

    public long getPasswordExpirationTimeout() {
        if (!checkActive()) {
            return Long.MAX_VALUE;
        }
        return policyManager.getPasswordExpirationTimeout(componentName);
    }

    public long getPasswordExpiration() {
        if (!checkActive()) {
            return Long.MAX_VALUE;
        }
        return policyManager.getPasswordExpiration(componentName);
    }

    public int getPasswordHistoryLength() {
        if (!checkActive()) {
            return 0;
        }
        return policyManager.getPasswordHistoryLength(componentName);
    }

    public int getPasswordMaximumLength(int quality) {
        if (!checkActive()) {
            return 0;
        }
        return policyManager.getPasswordMaximumLength(quality);
    }

    public boolean isActivePasswordSufficient() {
//        if (!checkActive()) {
//            return false;
//        }
        return policyManager.isActivePasswordSufficient();
    }

    public int getCurrentFailedPasswordAttempts() {
        if (!checkActive()) {
            return 0;
        }
        return policyManager.getCurrentFailedPasswordAttempts();
    }

//    public void setMaximumFailedPasswordsForWipe(int num) {
//        profile.setMaximumFailedPasswordsForWipe(num);
//
//        if (!checkActive()) {
//            return;
//        }
//        policyManager.setMaximumFailedPasswordsForWipe(componentName, num);
//    }

    public int getMaximumFailedPasswordsForWipe() {
        if (!checkActive()) {
            return Integer.MAX_VALUE;
        }
        return policyManager.getMaximumFailedPasswordsForWipe(componentName);
    }

    public boolean resetPassword(String password, int flags) {
        if (!checkActive()) {
            return false;
        }

        return policyManager.resetPassword(password, flags);
    }

    public void setMaximumTimeToLock(long timeMs) {
        if (!checkActive()) {
            return;
        }
        policyManager.setMaximumTimeToLock(componentName, timeMs);
    }

    public long getMaximumTimeToLock() {
        if (!checkActive()) {
            return Long.MAX_VALUE;
        }
        return policyManager.getMaximumTimeToLock(componentName);
    }

    public void lockNow() {
        if (!checkActive()) {
            return;
        }
        policyManager.lockNow();
    }

    public void wipeData(int flags) {
        if (!checkActive()) {
            return;
        }
        policyManager.wipeData(flags);
    }

    public void wipeDataAll() {
        wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
    }

    public int setStorageEncryption(boolean encrypt) {
        if (!checkActive()) {
            return 0;
        }
        return policyManager.setStorageEncryption(componentName, encrypt);
    }

    public boolean getStorageEncryption() {
        return policyManager.getStorageEncryption(componentName);
    }

    public int getStorageEncryptionStatus() {
        if (!checkActive()) {
            return 0;
        }
        return policyManager.getStorageEncryptionStatus();
    }

    public void setCameraDisabled(boolean disabled) {
        if (!checkActive()) {
            return;
        }
        policyManager.setCameraDisabled(componentName, disabled);
    }

    public boolean getCameraDisabled() {
        return policyManager.getCameraDisabled(componentName);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void setKeyguardDisabledFeatures(int which) {
        if (!EnvironmentUtil.SdkUtil.hasJellyBeanMr1()) {
            logger.error("Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);
            return;
        }

        if (!checkActive()) {
            return;
        }
        policyManager.setKeyguardDisabledFeatures(componentName, which);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public int getKeyguardDisabledFeatures() {
        if (!EnvironmentUtil.SdkUtil.hasJellyBeanMr1()) {
            logger.error("Build.VERSION.SDK_INT = " + Build.VERSION.SDK_INT);
            return 0;
        }

        if (!checkActive()) {
            return 0;
        }
        return policyManager.getKeyguardDisabledFeatures(componentName);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean isDeviceOwnerApp(String packageName) {
        if (!checkActive()) {
            return false;
        }

        return policyManager.isDeviceOwnerApp(packageName);
    }
}
