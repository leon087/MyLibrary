package cm.android.apn;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.telephony.TelephonyManager;

import cm.android.util.AndroidUtils;
import cm.java.util.IoUtil;

/**
 * <uses-permission android:name="android.permission.WRITE_APN_SETTINGS" />
 */
public class ApnUtil {

    private static final Logger logger = LoggerFactory.getLogger("apn");

    public static final Uri APN_URI = Uri.parse("content://telephony/carriers");

    public static final Uri PREFER_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    public static enum Apn {
        CMNET("cmnet"), CMWAP("cmwap");

        private String apn;

        private Apn(String apn) {
            this.apn = apn;
        }

        public String getApn() {
            return apn;
        }
    }

    public static int addAPN(Context context, ApnModel model) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();
        values.put("name", model.name);
        values.put("apn", model.apn);
        values.put("user", model.user);
        values.put("password", model.password);

        String simOperator = getSimOperator(context);
        String mnc = simOperator.substring(3, simOperator.length());
        String mcc = simOperator.substring(0, 3);
        values.put("mcc", mcc);
        values.put("mnc", mnc);
        values.put("numeric", simOperator);

        Cursor c = null;
        try {
            Uri newRow = resolver.insert(APN_URI, values);
            if (newRow == null) {
                return -1;
            }

            c = resolver.query(newRow, null, null, null, null);
            int idindex = c.getColumnIndex("_id");
            c.moveToFirst();
            int defaultAPNId = c.getShort(idindex);
            return defaultAPNId;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return -1;
        } catch (SecurityException e) {
            logger.error(e.getMessage(), e);
            return -1;
        } finally {
            AndroidUtils.closeQuietly(c);
        }
    }

    private static String getSimOperator(Context context) {
        TelephonyManager tm = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String SimOperator = tm.getSimOperator();
        return SimOperator;
    }

    //设置接入点
    public static boolean setApn(Context context, int apnId) {
        ContentResolver resolver = context.getContentResolver();
        ContentValues values = new ContentValues();

        values.put("apn_id", apnId);
        Cursor cursor = null;
        try {
            resolver.update(PREFER_APN_URI, values, null, null);
            cursor = resolver.query(PREFER_APN_URI, new String[]{"name", "apn"}, "_id=" + apnId, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getCount() > 0;
            }
            return false;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return false;
        } catch (SecurityException e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            AndroidUtils.closeQuietly(cursor);
        }
    }

//    public static int queryDefault(Context context) {
//        Cursor cursor = context.getContentResolver()
//                .query(PREFER_APN_URI, new String[]{"name", "apn"}, "_id=" + apnId, null, null);
//    }

    public static int queryByApn(Context context, String apn) {
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(APN_URI, null,
                    "apn like ? ", new String[]{apn}, null);
//        Cursor cursor = context.getContentResolver().query(APN_URI, null,
//                "apn like '%" + apn + "%' ", null, null);

            if (cursor != null && cursor.moveToFirst()) {
                int idindex = cursor.getColumnIndex("_id");
//            int idindex = cursor.getColumnIndex("apn_id");
                return cursor.getShort(idindex);
            }
        } catch (SecurityException e) {
            logger.error(e.getMessage(), e);
        } finally {
            AndroidUtils.closeQuietly(cursor);
        }
        return -1;
    }

//    /**
//     * 利用ContentProvider将添加的APN数据添加进入数据库
//     */
//    public int AddYidongApn(Context context) {
//        int apnId = -1;
//        ContentResolver resolver = context.getContentResolver();
//        ContentValues values = new ContentValues();
//
//        values.put("name", EM_APN[0]);
//        values.put("apn", EM_APN[1]);
//        values.put("type", EM_APN[4]);
//        values.put("numeric", NUMERIC);
//        values.put("mcc", NUMERIC.substring(0, 3));
//        values.put("mnc", NUMERIC.substring(3, NUMERIC.length()));
//        values.put("proxy", "");
//        values.put("port", "");
//        values.put("mmsproxy", "");
//        values.put("mmsport", "");
//        values.put("user", "");
//        values.put("server", "");
//        values.put("password", "");
//        values.put("mmsc", "");
//
//        Cursor c = null;
//
//        try {
//            Uri newRow = resolver.insert(APN_LIST_URI, values);
//            if (newRow != null) {
//                c = resolver.query(newRow, null, null, null, null);
//                int idindex = c.getColumnIndex("_id");
//                c.moveToFirst();
//                apnId = c.getShort(idindex);
//                Log.d("Robert", "New ID: " + apnId
//                        + ": Inserting new APN succeeded!");
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//
//        if (c != null) {
//            c.close();
//        }
//
//        return apnId;
//
//    }

//    /**
//     * 判断要设置的APN是否存在
//     * @param apnNode
//     * @return
//     */
//    public int IsYidongApnExisted(ApnNode apnNode) {
//        int apnId = -1;
//        Cursor mCursor = context.getContentResolver().query(APN_LIST_URI, null,
//                "apn like '%hnydz.ha%'", null, null);
//
//        while (mCursor != null && mCursor.moveToNext()) {
//            apnId = mCursor.getShort(mCursor.getColumnIndex("_id"));
//            String name = mCursor.getString(mCursor.getColumnIndex("name"));
//            String apn = mCursor.getString(mCursor.getColumnIndex("apn"));
//            String proxy = mCursor.getString(mCursor.getColumnIndex("proxy"));
//            String type = mCursor.getString(mCursor.getColumnIndex("type"));
//
//            if (apnNode.getName().equals(name)
//                    && (apnNode.getApn().equals(apn))
//                    && (apnNode.getName().equals(name))
//                    && (apnNode.getType().equals(type))) {
//                return apnId;
//            } else {
//                apnId = -1;
//            }
//        }
//
//        return apnId;
//    }
//
//    /**
//     * 转换APN状态
//     * 将CMNET切换为要设置的APN
//     */
//    public void SwitchApn() {
//        // 判断网络类型
//        switch (GetCurrentNetType()) {
//            case NET_3G:
//                // 如果3G网络则切换APN网络类型
//                if (!IsCurrentYidongApn()) {
//                    EM_APN_ID = IsYidongApnExisted(YIDONG_APN);
//
//                    if (EM_APN_ID == -1) {
//                        setDefaultApn(AddYidongApn());
//                    } else {
//                        setDefaultApn(EM_APN_ID);
//                    }
//                }
//                break;
//            case NET_WIFI:
//                // 如果是无线网络则转换为3G网络
//                closeWifiNetwork();
//                break;
//            case NET_OTHER:
//                // 如果是其他网络则转化为3G网络
//                break;
//            default:
//                break;
//        }
//    }
//
//    /**
//     * 获取当前网络类型
//     * @return
//     */
//    public int GetCurrentNetType() {
//        int net_type = getNetWorkType();
//
//        if (net_type == ConnectivityManager.TYPE_MOBILE) {
//            return NET_3G;
//        } else if (net_type == ConnectivityManager.TYPE_WIFI) {
//            return NET_WIFI;
//        }
//
//        return NET_OTHER;
//    }
//
//    /**
//     * 要设置的APN是否与当前使用APN一致
//     * @return
//     */
//    public boolean IsCurrentYidongApn() {
//        // 初始化移动APN选项信息
//        InitYidongApn();
//        YIDONG_OLD_APN = getDefaultAPN();
//
//        if ((YIDONG_APN.getName().equals(YIDONG_OLD_APN.getName()))
//                && (YIDONG_APN.getApn().equals(YIDONG_OLD_APN.getApn()))
//                && (YIDONG_APN.getType().equals(YIDONG_OLD_APN.getType()))) {
//            return true;
//        }
//
//        return false;
//    }
//
//    /**
//     * 初始化移动APN信息参数
//     */
//    protected void InitYidongApn() {
//        YIDONG_APN = new ApnNode();
//        YIDONG_APN.setName(EM_APN[0]);
//        YIDONG_APN.setApn(EM_APN[1]);
//        YIDONG_APN.setType(EM_APN[4]);
//    }
//
//    /**
//     * 获取当前使用的APN信息
//     * @return
//     */
//    public ApnNode getDefaultAPN() {
//        String id = "";
//        String apn = "";
//        String name = "";
//        String type = "";
//        ApnNode apnNode = new ApnNode();
//        Cursor mCursor = context.getContentResolver().query(PREFERRED_APN_URI,
//                null, null, null, null);
//
//        if (mCursor == null) {
//            return null;
//        }
//
//        while (mCursor != null && mCursor.moveToNext()) {
//            id = mCursor.getString(mCursor.getColumnIndex("_id"));
//            name = mCursor.getString(mCursor.getColumnIndex("name"));
//            apn = mCursor.getString(mCursor.getColumnIndex("apn"))
//                    .toLowerCase();
//            type = mCursor.getString(mCursor.getColumnIndex("type"))
//                    .toLowerCase();
//        }
//
//        try {
//            OLD_APN_ID = Integer.valueOf(id);
//        } catch (Exception e) {
//            // TODO: handle exception
//            Toast.makeText(context, "请配置好APN列表！", Toast.LENGTH_LONG).show();
//        }
//
//        apnNode.setName(name);
//        apnNode.setApn(apn);
//        apnNode.setType(type);
//
//        return apnNode;
//    }
//
//    /**
//     * 关闭APN，并设置成CMNET
//     */
//    public void StopYidongApn() {
//        if (IsCurrentYidongApn()) {
//            // 初始化CMNET
//            InitCMApn();
//            int i = IsCMApnExisted(CHINAMOBILE_APN);
//
//            if (i != -1) {
//                setDefaultApn(i);
//            }
//        }
//    }
//
//    /**
//     * 初始化默认的CMNET参数
//     */
//    protected void InitCMApn() {
//        GetNumeric();
//
//        CHINAMOBILE_APN = new ApnNode();
//        CHINAMOBILE_APN.setName(CM_APN[0]);
//        CHINAMOBILE_APN.setApn(CM_APN[1]);
//        CHINAMOBILE_APN.setType(CM_APN[4]);
//        CHINAMOBILE_APN.setMcc(NUMERIC.substring(0, 3));
//        CHINAMOBILE_APN.setMnc(NUMERIC.substring(3, NUMERIC.length()));
//    }
//
//    /**
//     * 判断CMNET是否存在
//     * @param apnNode
//     * @return
//     */
//    public int IsCMApnExisted(ApnNode apnNode) {
//        int apnId = -1;
//        Cursor mCursor = context.getContentResolver().query(APN_LIST_URI, null,
//                "apn like '%cmnet%' or apn like '%CMNET%'", null, null);
//
//        // 如果不存在CMNET，则添加。
//        if(mCursor == null){
//            addCmnetApn();
//        }
//
//        while (mCursor != null && mCursor.moveToNext()) {
//            apnId = mCursor.getShort(mCursor.getColumnIndex("_id"));
//            String name = mCursor.getString(mCursor.getColumnIndex("name"));
//            String apn = mCursor.getString(mCursor.getColumnIndex("apn"));
//            String proxy = mCursor.getString(mCursor.getColumnIndex("proxy"));
//            String type = mCursor.getString(mCursor.getColumnIndex("type"));
//
//            if ((apnNode.getApn().equals(apn)) && (apnNode.getType().indexOf(type) != -1)) {
//                return apnId;
//            } else {
//                apnId = -1;
//            }
//        }
//
//        return apnId;
//    }
}
