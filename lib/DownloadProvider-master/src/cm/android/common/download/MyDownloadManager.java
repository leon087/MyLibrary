package cm.android.common.download;

import android.database.Cursor;
import android.net.Uri;
import com.mozillaonline.providers.DownloadManager;
import com.mozillaonline.providers.DownloadManager.Query;

import java.io.File;

public class MyDownloadManager extends DownloadManagerPro {

    public void deleteDataAndFile(long id) {
        delete(id);
        Query query = new Query().setFilterById(id);
        deleteDataAndFile(query);
    }

    public void deleteDataAndFile(Query query) {
        Cursor cursor = super.query(query);
        if (cursor.moveToFirst()) {
            int mLocalUriColumnId = cursor
                    .getColumnIndexOrThrow(DownloadManager.COLUMN_LOCAL_URI);
            String uriString = cursor.getString(mLocalUriColumnId);

            int mIdColumnId = cursor
                    .getColumnIndexOrThrow(DownloadManager.COLUMN_ID);
            long id = cursor.getLong(mIdColumnId);
            delete(id);
            Uri uri = Uri.parse(uriString);
            File file = new File(uri.getPath());
            file.delete();
        }
    }
}
