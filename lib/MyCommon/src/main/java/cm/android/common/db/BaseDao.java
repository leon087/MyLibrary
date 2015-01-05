package cm.android.common.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class BaseDao {

    protected ContentResolver resolver = null;

    protected Uri contentUri;

    protected String[] projection;

    public BaseDao(Uri uri, String[] projection, Context context) {
        resolver = context.getContentResolver();
        this.contentUri = uri;
        this.projection = projection;
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

    public int delete(int id) {
        Uri uri = ContentUris.withAppendedId(contentUri, id);
        int count = resolver.delete(uri, null, null);
        return count;
    }

    public void upsert(ContentValues values, String selection, String[] selectionArgs) {
        Cursor cursor = query(selection, selectionArgs, null);
        try {
            if (cursor != null && cursor.moveToFirst()) {
                int id = parseId(cursor);
                update(id, values);
            } else {
                insert(values);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    protected Cursor query(String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor = resolver.query(contentUri, projection,
                selection, selectionArgs, sortOrder);
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
