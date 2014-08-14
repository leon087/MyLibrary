package cm.android.common.download;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import cm.android.sdk.BaseBroadcastReceiver;
import cm.android.util.MyLog;
import cm.android.util.ObjectUtil;
import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.DownloadManager.Query;
import com.mozillaonline.providers.DownloadManager.Request;
import com.mozillaonline.providers.downloads.DownloadService;

import java.util.Set;

public class DownloadManagerPro {
    private DownloadManager downloadManager;
    private Context context;
    private DownloadReceiver downloadReceiver;
    private volatile boolean isInit = false;
    private Set<OnCompleteListener> onCompleteListeners = ObjectUtil
            .newHashSet();
    private final OnCompleteListener listener = new OnCompleteListener() {
        @Override
        public void onDownloadSuccess(Query downloadQuery) {
            synchronized (onCompleteListeners) {
                for (OnCompleteListener onCompleteListener : onCompleteListeners) {
                    onCompleteListener.onDownloadSuccess(downloadQuery);
                }
            }
        }

        @Override
        public void onDownloadFailure(Query downloadQuery) {
            synchronized (onCompleteListeners) {
                for (OnCompleteListener onCompleteListener : onCompleteListeners) {
                    onCompleteListener.onDownloadFailure(downloadQuery);
                }
            }
        }
    };

    public static boolean isDownloadSuccess(int status) {
        if (status == DownloadManager.STATUS_SUCCESSFUL) {
            return true;
        }
        return false;
    }

    public static boolean isDownloadCompleted(int status) {
        if (status == DownloadManager.STATUS_FAILED
                || isDownloadSuccess(status)) {
            return true;
        }
        return false;
    }

    public void init(Context context) {
        if (isInit) {
            return;
        }
        this.context = context;
        downloadManager = new DownloadManager(context.getContentResolver(),
                context.getPackageName());
        downloadManager.setAccessAllDownloads(false);
        startDownloadService(context);
        downloadReceiver = new DownloadReceiver();
        downloadReceiver.registerReceiver(context);

        isInit = true;
    }

    public void deInit() {
        if (!isInit) {
            return;
        }
        isInit = false;

        clearListener();
        stopDownloadService(context);
        downloadReceiver.unRegisterReceiver();
        context = null;
    }

    private void startDownloadService(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DownloadService.class);
        context.startService(intent);
    }

    private void stopDownloadService(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, DownloadService.class);
        context.stopService(intent);
    }

    public DownloadManager.Request getDefaultRequest(String url) {
        Uri srcUri = Uri.parse(url);
        DownloadManager.Request request = new Request(srcUri);
        request.addRequestHeader("User-Agent", "Android"); // 添加一个Http请求报头，

        // 设置允许使用的网络类型，这一步Android2.3做的很好，目前有两种定义分别为NETWORK_MOBILE和NETWORK_WIFI我们可以选择使用移动网络或Wifi方式来下载。
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                | DownloadManager.Request.NETWORK_WIFI);

        // 对于下载，考虑到流量费用，这里是否允许使用漫游。
        request.setAllowedOverRoaming(false);

        request.setShowRunningNotification(true); // 是否显示下载进度的提示
        // request.setTitle("Downloading"); //设置notification的标题
        // 设置一个描述信息，主要是最终显示的notification提示，可以随便写个自己区别
        // request.setDescription("Downloading");

        // 设置外部存储的公共目录，一般通过getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)方法获取。
        // request.setDestinationInExternalPublicDir(
        // Environment.DIRECTORY_DOWNLOADS, "/");
        // 设置目标存储在外部目录，一般位置可以用 getExternalFilesDir()方法获取。
        request.setDestinationInExternalFilesDir(context,
                Environment.DIRECTORY_DOWNLOADS, "/");

        // String filename = url.substring(url.lastIndexOf("/") + 1);
        // filename =path.getPath() + "/" + filename;
        // if (isFileExist(path, filename)) {
        // // StartapkinstallIntesnt();
        // return;
        // }
        // Uri dpath = Uri.fromFile(new File(path.getPath() + "/" + filename));
        // 设置下载后文件路径
        // request.setDestinationUri(dpath);

        // 设置mime类型，这里看服务器配置，一般国家化的都为utf-8编码。
        // request.setMimeType(String mimeType)

        // request.setVisibleInDownloadsUi(true); //设置下载管理类在处理过程中的界面是否显示
        return request;

    }

    public long start(String url) {
        DownloadManager.Request request = getDefaultRequest(url);
        return start(request);
    }

    public long start(DownloadManager.Request request) {
        // request.setVisibleInDownloadsUi(true); //设置下载管理类在处理过程中的界面是否显示
        long enqueue = downloadManager.enqueue(request);
        return enqueue;
    }

    /**
     * 暂停
     *
     * @param ids
     */
    public void pause(long... ids) {
        downloadManager.pauseDownload(ids);
    }

    /**
     * 继续
     *
     * @param ids
     */
    public void resume(long... ids) {
        downloadManager.resumeDownload(ids);
    }

    /**
     * 删除
     *
     * @param ids
     */
    public void delete(long... ids) {
        downloadManager.remove(ids);
    }

    /**
     * 重新下载，下载完成后才能调用（成功或失败）
     *
     * @param ids
     */
    public void restart(long... ids) {
        downloadManager.restartDownload(ids);
    }

    ;

    public int getStatus(long downloadId) {
        int status = 0;
        DownloadManager.Query query = new DownloadManager.Query()
                .setFilterById(downloadId);
        Cursor c = null;
        try {
            c = downloadManager.query(query);
            if (c != null && c.moveToFirst()) {
                status = c.getInt(c
                        .getColumnIndex(DownloadManager.COLUMN_STATUS));
            }
        } finally {
            if (c != null) {
                c.close();
            }
        }
        return status;
    }

    public Query query(long id) {
        Query myDownloadQuery = new Query();
        myDownloadQuery.setFilterById(id);
        // Cursor cursor = downloadManager.query(myDownloadQuery);
        return myDownloadQuery;
    }

    public Cursor query(Query query) {
        return downloadManager.query(query);
    }

    public void addOnCompleteListener(OnCompleteListener onCompleteListener) {
        synchronized (onCompleteListeners) {
            onCompleteListeners.add(onCompleteListener);
        }
    }

    public void removeOnCompleteListener(OnCompleteListener onCompleteListener) {
        synchronized (onCompleteListeners) {
            onCompleteListeners.remove(onCompleteListener);
        }
    }

    private void clearListener() {
        synchronized (onCompleteListeners) {
            onCompleteListeners.clear();
        }
    }

    public static interface OnCompleteListener {
        void onDownloadSuccess(Query downloadQuery);

        void onDownloadFailure(Query downloadQuery);
    }

    private class DownloadReceiver extends BaseBroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(
                    DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            Query myDownloadQuery = query(reference);
            Cursor cursor = downloadManager.query(myDownloadQuery);
            if (cursor.moveToFirst()) {
                int mStatusColumnId = cursor
                        .getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS);

                int status = cursor.getInt(mStatusColumnId);
                MyLog.i("status = " + status);
                if (status == DownloadManager.STATUS_SUCCESSFUL) {
                    listener.onDownloadSuccess(myDownloadQuery);
                } else if (status == DownloadManager.STATUS_FAILED) {
                    listener.onDownloadFailure(myDownloadQuery);
                } else {

                }
                // int fileNameIdx = myDownload
                // .getColumnIndex(DownloadManager.COLUMN_LOCAL_FILENAME);
                // int fileUriIdx = cursor
                // .getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                // String fileUri = cursor.getString(fileUriIdx);
                // 判断下载成功/失败
            }
            cursor.close();
        }

        @Override
        public IntentFilter createIntentFilter() {
            IntentFilter filter = super.createIntentFilter();
            // 监听下载完成（包括成功或失败）
            filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            return filter;
        }
    }
}
