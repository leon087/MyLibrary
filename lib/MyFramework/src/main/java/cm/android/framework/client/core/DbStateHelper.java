package cm.android.framework.client.core;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import cm.java.util.IoUtil;

public class DbStateHelper {

    private static final String STATE_FILE_NAME = "framework_app_status";

    private static final String TAG_STATE = "state";

    private static final String ID = "_id";

    private static final String DB_NAME = "framework.db";

    public static boolean isStateInit(Context context) {
        return readState(context);
    }

    public static class OpenHelper extends SQLiteOpenHelper {

        public static final int VERSION = 1;

        public OpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
            super(context, name, factory, VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_TABLE = "create table if not exists " + STATE_FILE_NAME
                    + "(" + ID + " integer primary key autoincrement,"
                    + TAG_STATE + " text)";
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

    public static void writeState(Context context, boolean state) {
        LogUtil.getLogger().info("StateHolder:writeState:state = " + state);

        DbStateHelper.OpenHelper helper = new DbStateHelper.OpenHelper(context, DB_NAME, null);
        SQLiteDatabase dbw = helper.getWritableDatabase();
        SQLiteDatabase dbr = helper.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TAG_STATE, String.valueOf(state));

        Cursor cursor = null;
        try {
            cursor = dbr.query(STATE_FILE_NAME, null, null, null, null, null, ID + " asc");
            if (null == cursor || !cursor.moveToFirst()) {
                //查询不到表明未插入
                dbw.insert(STATE_FILE_NAME, "false", cv);
            } else {
                //已插入，更新值
                String whereClause = " " + ID + " = ? ";
                String[] whereArgs = {String.valueOf(1)};
                dbw.update(STATE_FILE_NAME, cv, whereClause, whereArgs);
            }
        } catch (Exception e) {
            LogUtil.getLogger().error(e.getMessage());
        } finally {
            IoUtil.closeQuietly(cursor);
        }
    }

    public static boolean readState(Context context) {
        DbStateHelper.OpenHelper helper = new DbStateHelper.OpenHelper(context, DB_NAME, null);
        SQLiteDatabase dbr = helper.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = dbr.query(STATE_FILE_NAME, null, null, null, null, null, ID + " asc");
            if (null == cursor || !cursor.moveToFirst()) {
                //查询不到表明未插入
                LogUtil.getLogger().info("StateHolder:writeState:state = false,cursor = null");
                return false;
            }

            boolean state = Boolean.valueOf(cursor.getString(cursor.getColumnIndex(TAG_STATE)));
            LogUtil.getLogger().info("StateHolder:writeState:state = {}", state);
            return state;
        } catch (Exception e) {
            LogUtil.getLogger().error("StateHolder:writeState:state = false,error = {}", e.getMessage());
            return false;
        } finally {
            IoUtil.closeQuietly(cursor);
        }
    }
}
