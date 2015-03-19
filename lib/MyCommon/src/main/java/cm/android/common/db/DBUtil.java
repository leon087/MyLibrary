package cm.android.common.db;

import android.database.Cursor;

public class DBUtil {

    public static void closeQuietly(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }
}
