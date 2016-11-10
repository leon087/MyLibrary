package cm.android.common.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class BaseDao {
    private static final Logger logger = LoggerFactory.getLogger("db");

    protected ContentResolver resolver = null;

    protected Uri contentUri;

    protected String[] projection;

    public void register(Context context, ContentObserver observer) {
        context.getContentResolver()
                .registerContentObserver(contentUri, true, observer);
    }

    public void unregister(Context context, ContentObserver observer) {
        context.getContentResolver().unregisterContentObserver(observer);
    }

    public BaseDao(Uri uri, String[] projection, Context context) {
        resolver = context.getContentResolver();
        this.contentUri = uri;
        this.projection = projection.clone();
    }

    public int deleteAll() {
        int count = delete(null, null);
        return count;
    }

    protected long insert(ContentValues values) {
        Uri uri = resolver.insert(contentUri, values);
        String itemId = uri.getPathSegments().get(1);
        return Integer.valueOf(itemId).longValue();
    }

    public int update(int id, ContentValues values) {
        Uri uri = ContentUris.withAppendedId(contentUri, id);
        int count = resolver.update(uri, values, null, null);
        return count;
    }

    public int update(ContentValues values, String where, String[] selectionArgs) {
        int count = resolver.update(contentUri, values, where, selectionArgs);
        return count;
    }

    public int delete(int id) {
        int count = delete(id, null, null);
        return count;
    }

    public int delete(int id, String where, String[] selectionArgs) {
        Uri uri = ContentUris.withAppendedId(contentUri, id);
        int count = delete(uri, where, selectionArgs);
        return count;
    }

    public int delete(Uri uri, String where, String[] selectionArgs) {
        int count = resolver.delete(uri, where, selectionArgs);
        return count;
    }

    public void upsert(ContentValues values, String selection, String[] selectionArgs) {
        Cursor cursor = null;
//        try {
        cursor = query(selection, selectionArgs, null);
//        } catch (RuntimeException e) {
//            logger.error(e.getMessage(), e);
//        }
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int id = parseId(cursor);
                update(id, values);
            } else {
                insert(values);
            }
        } finally {
            DBUtil.closeQuietly(cursor);
        }
    }

    protected Cursor query(String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = resolver.query(contentUri, projection, selection, selectionArgs, sortOrder);
        return cursor;
    }

    protected int delete(String selection, String[] selectionArgs) {
        int count = resolver.delete(contentUri, selection, selectionArgs);
        return count;
    }

    protected int parseId(Cursor cursor) {
        int id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
        return id;
    }
}
