package cm.android.app.manager;

import android.net.Uri;
import android.os.Handler;
import cm.android.app.global.Tag;
import cm.android.common.http.HttpUtil;
import cm.android.framework.BaseApp;
import cm.android.util.util.*;
import com.loopj.android.http.ApkHttpResponseHandler;
import org.apache.http.Header;

import java.io.File;
import java.util.Map;

public class UpgradeManager {
    /**
     * 不需要升级
     */
    public static final int UPGRADE_NONEED = -1;
    /**
     * 需要升级
     */
    public static final int UPGRADE = 3;

    /**
     * 确认升级对话框
     */
    public static final int TYPE_DIALOG_UPGRADE = 0;
    /**
     * 正在下载对话框
     */
    public static final int TYPE_DIALOG_DOWNLOAD = 1;
    /**
     * 失败对话框
     */
    public static final int TYPE_DIALOG_FAILED = 2;
    /**
     * 正在安装
     */
    public static final int TYPE_DIALOG_INSTALL = 3;

    /**
     * 软件版本升级handler实例
     */
    private Handler uiHandler = null;
    private Map<String, Object> upgradeInfo = null;

    private static final String APK_LOCAL_NAME = "update.apk";

    /**
     * 软件版本升级管理构造方法
     *
     * @param context
     * @param upgradeHandler
     */
    public UpgradeManager(Handler softUpdateHandler) {
        this.uiHandler = softUpdateHandler;
    }

    public void checkSoftUpadte(Map<String, Object> upgradeInfo) {
        this.upgradeInfo = upgradeInfo;
        int code = MapUtil.getInt(upgradeInfo, Tag.CODE);
        if (UPGRADE == code) {
            String upgradeUrl = MapUtil.getString(upgradeInfo, Tag.UPGRADE_URL);
            if (!Utils.isEmpty(upgradeUrl)) {
                // 升级
                sendMsg(TYPE_DIALOG_UPGRADE, UPGRADE);
                return;
            }
        }
        // 不升级
        sendMsg(TYPE_DIALOG_UPGRADE, UPGRADE_NONEED);
    }

    private void sendMsg(int what, Object obj) {
        uiHandler.obtainMessage(what, obj).sendToTarget();
    }

    /**
     * 执行升级
     */
    public void upgrade() {
        String clientUpdateUrl = MapUtil
                .getString(upgradeInfo, Tag.UPGRADE_URL);
        if (canUpdateApk()) {
            downloadApk(clientUpdateUrl);
        }
    }

    /**
     * 下载apk包的请求
     *
     * @param updateUrl
     */
    public void downloadApk(String updateUrl) {
        MyLog.i("updateUrl = " + updateUrl);
        if (Utils.isEmpty(updateUrl)) {
            // TODO
        } else {
            // // 下载
            ApkHttpResponseHandler apkResponseHandler = new ApkHttpResponseHandler(
                    BaseApp.getApp(), APK_LOCAL_NAME) {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      File file) {
                    uiHandler.sendEmptyMessage(TYPE_DIALOG_INSTALL);
                    IntentUtil.installPackage(BaseApp.getApp(),
                            Uri.fromFile(file));
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      Throwable throwable, File file) {
                    uiHandler.sendEmptyMessage(TYPE_DIALOG_FAILED);
                }

                @Override
                public void onProgress(int bytesWritten, int totalSize) {
                    // super.onProgress(bytesWritten, totalSize);
                    // uiHandler.sendMessage(uiHandler.obtainMessage(
                    // TYPE_DIALOG_SOFTDOAN, percentMap));
                }
            };
            apkResponseHandler.deleteTargetFile();
            HttpUtil.exec(updateUrl, apkResponseHandler);
        }
    }

    /**
     * 判断是否能够升级
     *
     * @return
     */
    private boolean canUpdateApk() {
        File file = EnvironmentInfo.getDataStorageDirectory(BaseApp.getApp());
        if (EnvironmentInfo.hasEnoughSpace(file)) {
            return true;
        } else {
            return false;
        }
    }
}
