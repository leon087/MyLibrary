//package cm.android.util;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import android.content.Context;
//import android.telephony.TelephonyManager;
//import android.text.TextUtils;
//
//import java.lang.reflect.Constructor;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//
////import android.annotation.SuppressLint;
//
//public class SimUtil {
//
//    public static class SimInfo {
//
//        public int slotNum;
//
//        public String slotName;
//
//        public String IMSI;
//
//        public String IMEI;
//
//        public String SIMSerial;
//
//        public SimInfo() {
//            slotName = "null";
//        }
//
//        @Override
//        public String toString() {
//            return "SimInfo{" +
//                    "slotNum=" + slotNum +
//                    ", slotName='" + slotName + '\'' +
//                    ", IMSI='" + IMSI + '\'' +
//                    ", IMEI='" + IMEI + '\'' +
//                    ", SIMSerial='" + SIMSerial + '\'' +
//                    '}';
//        }
//    }
//
//    private static final Logger logger = LoggerFactory.getLogger("SimUtil");
//
//    public SimInfo sim1Info;
//
//    public SimInfo sim2Info;
//
//    private Context mContext;
//
//    public final static String m_IMEI = "getDeviceId";
//
//    public final static String m_IMSI = "getSubscriberId";
//
//    public final static String m_SIM_SERIAL = "getSimSerialNumber";
//
//    public final static String m_SIM_SUBSCRIBER = "getSubscriberId";
//
//    /*
//        *   getDeviceIdGemini , may work for mtk chip
//        *
//        *  getDeviceIdDs, work for Samsung Duos device
//        *  getSimSerialNumberGemini, work for Lenovo A319
//        * */
//    private static String[] deviceIdMethods = {"getDeviceIdGemini", "getDeviceId", "getDeviceIdDs",
//            "getDeviceIdExt"};
//
//    /*
//       *   getSimStateGemini , may work for mtk chip
//       *
//       *  getIccState, work for HTC device
//       *
//       * */
//    private static String[] networkTypeMethods = {"getSimStateGemini", "getSimState",
//            "getIccState"};
//
//    /*
//       *   getNetworkTypeGemini , may work for mtk chip
//       *
//       *  getNetworkTypeExt, work for HTC device
//       *
//       * */
//    private static String[] simStatusMethods = {"getNetworkTypeGemini", "getNetworkType",
//            "getNetworkTypeExt "};
//
//    /*
//       *   getNetworkOperatorNameGemini , may work for mtk chip
//       *
//       *  getNetworkOperatorNameExt, work for HTC device
//       *
//       * */
//    private static String[] networkOperatorNameMethods = {"getNetworkOperatorNameGemini",
//            "getNetworkOperatorName", "getNetworkOperatorNameExt "};
//
//    protected static CustomTelephony customTelephony;
//
//    private static MdmDualSimManager instance;
//
//    public static MdmDualSimManager getInstance(Context context) {
//        if (instance == null) {
//            synchronized (MdmDualSimManager.class) {
//                if (instance == null) {
//                    instance = new MdmDualSimManager(context);
//                }
//            }
//        }
//        return instance;
//    }
//
//    public SimInfo getSim1Info() {
//        return sim1Info;
//    }
//
//    public SimInfo getSim2Info() {
//        return sim2Info;
//    }
//
//    public SimUtil(Context mContext) {
//        this.mContext = mContext;
////        connInfo = new MyConnectivityManager(mContext);
//
//        sim1Info = new SimInfo();
//        sim1Info.slotNum = 0;
//        sim2Info = new SimInfo();
//        sim2Info.slotNum = 1;
//
//        collectSimInfo();
//    }
//
//    public static void resetInstance() {
//        synchronized (MdmDualSimManager.class) {
//            instance = null;
//        }
//    }
//
//    public void collectSimInfo() {
//        try {
//            if (sim1Info.IMEI == null) {
//                customTelephony = new CustomTelephony(mContext);
//            } else {
//                customTelephony.getCurrentData();
//            }
//
//            if (customTelephony.getIMEIList().size() > 0) {
//                customTelephony.getDefaultSIMInfo();
//            }
//
////            updateOperatorDetails();
//        } catch (Exception e) {
//            logger.error("" + e);
//        }
//    }
//
//    public boolean isDualSIMSupported() {
//        if (!TextUtils.isEmpty(sim1Info.IMEI) && !TextUtils.isEmpty(sim2Info.IMEI)
//                && !(sim1Info.IMEI.equals(sim2Info.IMEI))) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    public boolean isSecondSimActive() {
//        if (sim2Info.IMSI == null || TextUtils.isEmpty(sim2Info.IMSI)) {
//            return false;
//        } else if (sim1Info.IMSI != null && sim1Info.IMSI.equalsIgnoreCase(sim2Info.IMSI)) {
//            return false;
//        } else {
//            return true;
//        }
//
//    }
//
//    static class CustomTelephony {
//
//        Context mContext;
//
//        TelephonyManager telephony;
//
//        public CustomTelephony(Context mContext) {
//            try {
//                this.mContext = mContext;
//                telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//                updateInfos();
//
//            } catch (Exception e) {
//                //e.printStackTrace();
//                logger.error("" + e);
//            }
//        }
//
//        public void updateInfos() {
//            try {
//                fetchClassInfo();
//                gettingAllMethodValues();
//            } catch (Exception e) {
//                logger.error("" + e);
//            }
//        }
//
//        /**
//         * This method returns the class name in which we fetch dual sim details
//         */
//        public void fetchClassInfo() {
//            try {
//
//                for (int index = 0; index < listofClass.length; index++) {
//                    if (isTelephonyClassExists(listofClass[index])) {
//
//                        for (String deviceIdMethod : deviceIdMethods) {
//                            if (isMethodExists(listofClass[index], deviceIdMethod)) {
//                                //System.out.println("getDeviceId method found");
//                                if (!SIM_VARINT.equalsIgnoreCase("")) {
//                                    break;
//                                }
//                            }
//                        }
//
//                        for (String networkOperatorNameMethod : networkOperatorNameMethods) {
//                            if (isMethodExists(listofClass[index], networkOperatorNameMethod)) {
//                                //System.out.println("getNetworkOperatorName method found");
//                                if (!SIM_VARINT.equalsIgnoreCase("")) {
//                                    break;
//                                }
//                            }
//                        }
//                    }
//                }
//
//
//                for (int index = 0; index < listofClass.length; index++) {
//                    try {
//                        if (sim1Info.slotName == null || sim1Info.slotName.equalsIgnoreCase("")) {
//                            getValidSlotFields(listofClass[index]);
//                            // if(sim1Info.slotName!=null || !sim1Info.slotName.equalsIgnoreCase("")){
//                            getSlotNumber(listofClass[index]);
//                        } else {
//                            break;
//                        }
//                    } catch (Exception e) {
//                        //e.printStackTrace();
//                        logger.error("" + e);
//                    }
//                }
//            } catch (Exception e) {
//                //e.printStackTrace();
//                logger.error("" + e);
//            }
//        }
//
//        //slot number
//        public void initSlotName(Class<?> telephonyClass) {
//            Class<?>[] parameter = new Class[1];
//            parameter[0] = int.class;
//            Field[] fieldList = telephonyClass.getDeclaredFields();
//
//            for (int index = 0; index < fieldList.length; index++) {
//                Class<?> type = fieldList[index].getType();
//                if (!int.class.equals(type)) {
//                    continue;
//                }
//
//                String variableName = fieldList[index].getName();
//                if (variableName.contains("SLOT") || variableName.contains("slot")) {
//                    logger.info("ggg variableName = {}", variableName);
//
//                    //0|1
//                    //1|2
//                    if (variableName.contains("0")) {
//                        FIELD_SLOT_NAME_0 = variableName;
//                    } else if (variableName.contains("2")) {
//                        FIELD_SLOT_NAME_1 = variableName;
//                    } else if (variableName.contains("1")) {
//                        if (TextUtils.isEmpty(FIELD_SLOT_NAME_0)) {
//                            FIELD_SLOT_NAME_0 = variableName;
//                        } else {
//                            FIELD_SLOT_NAME_1 = variableName;
//                        }
//                    }
//                }
//
//                if (!TextUtils.isEmpty(FIELD_SLOT_NAME_0) && !TextUtils.isEmpty(FIELD_SLOT_NAME_1)) {
//                    return;
//                }
//            }
//        }
//
//        public String getMethodValue(String className, String compairMethod, int slotNum) {
//            String value = "";
//            try {
//                Class<?> telephonyClass = Class.forName(className);
//                Class<?>[] parameter = new Class[1];
//                parameter[0] = int.class;
//                StringBuffer sbf = new StringBuffer();
//                Method[] methodList = telephonyClass.getDeclaredMethods();
//                for (int index = methodList.length - 1; index >= 0; index--) {
//                    sbf.append("\n\n" + methodList[index].getName());
//                    if (methodList[index].getReturnType().equals(String.class)) {
//                        String methodName = methodList[index].getName();
//                        if (methodName.contains(compairMethod)) {
//                            Class<?>[] param = methodList[index]
//                                    .getParameterTypes();
//                            if (param.length > 0) {
//                                if (param[0].equals(int.class)) {
//                                    try {
//                                        SIM_VARINT = methodName.substring(
//                                                compairMethod.length(),
//                                                methodName.length());
//                                        if (!methodName.equalsIgnoreCase(compairMethod + "Name")
//                                                && !methodName.equalsIgnoreCase(
//                                                compairMethod + "ForSubscription")) {
//                                            value = invokeMethod(telephonyClassName,
//                                                    slotNum,
//                                                    compairMethod, SIM_VARINT);
//                                            if (!TextUtils.isEmpty(value)) {
//                                                break;
//                                            }
//                                        }
//                                    } catch (Exception e) {
//                                        //e.printStackTrace();
//                                        logger.error("" + e);
//                                    }
//                                } else if (param[0].equals(long.class)) {
//                                    try {
//                                        SIM_VARINT = methodName.substring(
//                                                compairMethod.length(),
//                                                methodName.length());
//                                        if (!methodName.equalsIgnoreCase(compairMethod + "Name")
//                                                && !methodName.equalsIgnoreCase(
//                                                compairMethod + "ForSubscription")) {
//                                            value = invokeLongMethod(telephonyClassName,
//                                                    slotNum, compairMethod, SIM_VARINT);
//                                            if (!TextUtils.isEmpty(value)) {
//                                                break;
//                                            }
//                                        }
//                                    } catch (Exception e) {
//                                        //e.printStackTrace();
//                                        logger.error("" + e);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                //e.printStackTrace();
//                logger.error("" + e);
//            }
//            return value;
//        }
//
//
//        /**
//         * Check method with sim variant
//         */
//        public boolean checkMethod(Class<?> telephonyClass, String compairMethod) {
////            try {
//            Class<?>[] parameter = new Class[1];
//            parameter[0] = int.class;
//            Method[] methodList = telephonyClass.getDeclaredMethods();
//
//            for (int index = methodList.length - 1; index >= 0; index--) {
//                Method method = methodList[index];
//                if (!String.class.equals(method.getReturnType())) {
//                    continue;
//                }
//
//                String methodName = method.getName();
//                if (!methodName.contains(compairMethod)) {
//                    continue;
//                }
//
//                logger.info("ggg methodName = {}", methodName);
//
//                Class<?>[] param = method.getParameterTypes();
//                if (param.length <= 0) {
//                    return false;
//                }
//
//                sTelephonyClass = telephonyClass;
//                if (param[0].equals(int.class)) {
//                    SIM_VARINT = methodName.substring(compairMethod.length(), methodName.length());
////                                        telephonyClassName = className;
//                    return true;
//                } else {
//                    return true;
//                }
//            }
////            } catch (Exception e) {
////                //e.printStackTrace();
////                logger.error("" + e);
////            }
//            return false;
////            return isExists;
//        }
//
//        public ArrayList<String> getIMEIList() {
//            ArrayList<String> imeiList = new ArrayList<>();
//            try {
//                sim1Info.IMEI = invokeMethod(telephonyClassName, sim1Info.slotNum, m_IMEI,
//                        SIM_VARINT);
//                if (TextUtils.isEmpty(sim1Info.IMEI)) {
//                    sim1Info.IMEI = getMethodValue(telephonyClassName, m_IMEI, sim1Info.slotNum);
//                }
//                if (sim1Info.IMEI == null || sim1Info.IMEI.equalsIgnoreCase("")) {
//                    sim1Info.IMEI = telephony.getDeviceId();
//                }
//                sim2Info.IMEI = invokeMethod(telephonyClassName, sim2Info.slotNum, m_IMEI,
//                        SIM_VARINT);
//                if (TextUtils.isEmpty(sim2Info.IMEI)) {
//                    sim2Info.IMEI = getMethodValue(telephonyClassName, m_IMEI, sim1Info.slotNum);
//                }
//            } catch (Exception e) {
//                logger.error("" + e);
//            }
//            if (!TextUtils.isEmpty(sim2Info.IMEI)) {
//                imeiList.add(sim1Info.IMEI);
//            }
//            if (!TextUtils.isEmpty(sim2Info.IMEI)) {
//                imeiList.add(sim2Info.IMEI);
//            }
//            return imeiList;
//        }
//
//        public void getDefaultSIMInfo() {
//            telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//            String IMSI = telephony.getSubscriberId();
//            if (TextUtils.isEmpty(sim1Info.IMSI) || sim1Info.IMSI.equalsIgnoreCase(IMSI)) {
//                sim1Info.IMEI = telephony.getDeviceId();
//            } else if (isSecondSimActive() && sim2Info.IMSI.equalsIgnoreCase(IMSI)) {
//                sim2Info.IMEI = telephony.getDeviceId();
//            }
//        }
//
//        public void gettingAllMethodValues() {
//            try {
//                sim1Info.IMEI = invokeMethod(telephonyClassName, sim1Info.slotNum, m_IMEI,
//                        SIM_VARINT);
//                if (TextUtils.isEmpty(sim1Info.IMEI)) {
//                    sim1Info.IMEI = getMethodValue(telephonyClassName, m_IMEI, sim1Info.slotNum);
//                }
//                if (sim1Info.IMEI == null || sim1Info.IMEI.equalsIgnoreCase("")) {
//                    sim1Info.IMEI = telephony.getDeviceId();
//                }
//                sim2Info.IMEI = invokeMethod(telephonyClassName, sim2Info.slotNum, m_IMEI,
//                        SIM_VARINT);
//                if (TextUtils.isEmpty(sim2Info.IMEI)) {
//                    sim2Info.IMEI = getMethodValue(telephonyClassName, m_IMEI, sim1Info.slotNum);
//                }
//                sim1Info.IMSI = invokeMethod(telephonyClassName, sim1Info.slotNum, m_IMSI,
//                        SIM_VARINT);
//                if (TextUtils.isEmpty(sim1Info.IMSI)) {
//                    sim1Info.IMSI = getMethodValue(telephonyClassName, m_IMSI, sim1Info.slotNum);
//                }
//                if (sim1Info.IMSI == null || sim1Info.IMSI.equalsIgnoreCase("")) {
//                    sim1Info.IMSI = telephony.getSubscriberId();
//                }
//                sim2Info.IMSI = invokeMethod(telephonyClassName, sim2Info.slotNum, m_IMSI,
//                        SIM_VARINT);
//                if (TextUtils.isEmpty(sim2Info.IMSI)) {
//                    sim2Info.IMSI = getMethodValue(telephonyClassName, m_IMSI, sim2Info.slotNum);
//                    if (TextUtils.isEmpty(sim2Info.IMSI)) {
//                        sim2Info.IMSI = getMethodValue(telephonyClassName, m_IMSI,
//                                sim2Info.slotNum + 1);
//                    }
//                }
//                if (!TextUtils.isEmpty(sim2Info.IMSI) && !TextUtils.isEmpty(sim1Info.IMSI)) {
//                    if (sim1Info.IMSI.equalsIgnoreCase(sim2Info.IMSI)) {
//                        sim1Info.IMSI = "";
//                    }
//                }
//                if (sim1Info.IMSI != null && sim2Info.IMSI != null && sim1Info.IMSI
//                        .equalsIgnoreCase("")) {
//                    String IMSI2 = getMethodValue(telephonyClassName, m_IMSI, sim2Info.slotNum + 1);
//                    if (!TextUtils.isEmpty(IMSI2)) {
//                        sim1Info.IMSI = sim2Info.IMSI;
//                        sim2Info.IMSI = IMSI2;
//                        sim1Info.slotNum = sim2Info.slotNum;
//                        sim2Info.slotNum = sim2Info.slotNum + 1;
//                    }
//                }
//                sim1Info.SIMSerial = getSimSerialNumber(sim1Info.slotNum);
//                sim2Info.SIMSerial = getSimSerialNumber(sim2Info.slotNum);
//                if (sim1Info.SIMSerial.equalsIgnoreCase(sim2Info.SIMSerial)) {
//                    sim2Info.SIMSerial = getSimSerialNumber(sim2Info.slotNum + 1);
//                }
//                getCurrentData();
//            } catch (Exception e) {
//                logger.error("" + e);
//            }
//        }
//
//        public String getSimSerialNumber(int slotNumber) {
//            String SIMSerial = invokeMethod(telephonyClassName, slotNumber, m_SIM_SUBSCRIBER,
//                    SIM_VARINT);
//            if (TextUtils.isEmpty(SIMSerial)) {
//                SIMSerial = getMethodValue(telephonyClassName, m_SIM_SUBSCRIBER, slotNumber);
//                if (TextUtils.isEmpty(SIMSerial)) {
//                    SIMSerial = getMethodValue(telephonyClassName, m_SIM_SERIAL, slotNumber);
//                }
//                if (TextUtils.isEmpty(SIMSerial)) {
//                    SIMSerial = getMethodValue(telephonyClassName, m_SIM_SERIAL, slotNumber);
//                }
//            }
//            return SIMSerial;
//        }
//
//        private void getCurrentData() {
//            try {
//                telephony = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
//            } catch (Exception e) {
//                logger.error("" + e);
//            }
//        }
//
//        private String invokeMethod(String className, int slotNumber,
//                                    String methodName, String SIM_variant) {
//            String value = "";
//
//            try {
//                Class<?> telephonyClass = Class.forName(className);
//                Constructor[] cons = telephonyClass.getDeclaredConstructors();
//                cons[0].getName();
//                cons[0].setAccessible(true);
//                Object obj = cons[0].newInstance();
//                Class<?>[] parameter = new Class[1];
//                parameter[0] = int.class;
//                Object ob_phone = null;
//                try {
//                    Method getSimID = telephonyClass.getMethod(methodName
//                            + SIM_variant, parameter);
//                    Object[] obParameter = new Object[1];
//                    obParameter[0] = slotNumber;
//                    ob_phone = getSimID.invoke(obj, obParameter);
//                } catch (Exception e) {
//                    if (slotNumber == 0) {
//                        Method getSimID = telephonyClass.getMethod(methodName
//                                + SIM_variant, parameter);
//                        Object[] obParameter = new Object[1];
//                        obParameter[0] = slotNumber;
//                        ob_phone = getSimID.invoke(obj);
//                    }
//                }
//
//                if (ob_phone != null) {
//                    value = ob_phone.toString();
//                }
//            } catch (Exception e) {
//                invokeOldMethod(className, slotNumber, methodName, SIM_variant);
//            }
//
//            return value;
//        }
//
//        private String invokeLongMethod(String className, long slotNumber,
//                                        String methodName, String SIM_variant) {
//            String value = "";
//
//            try {
//                Class<?> telephonyClass = Class.forName(className);
//                Constructor[] cons = telephonyClass.getDeclaredConstructors();
//                cons[0].getName();
//                cons[0].setAccessible(true);
//                Object obj = cons[0].newInstance();
//                Class<?>[] parameter = new Class[1];
//                parameter[0] = long.class;
//                Object ob_phone = null;
//                try {
//                    Method getSimID = telephonyClass.getMethod(methodName
//                            + SIM_variant, parameter);
//                    Object[] obParameter = new Object[1];
//                    obParameter[0] = slotNumber;
//                    ob_phone = getSimID.invoke(obj, obParameter);
//                } catch (Exception e) {
//                    if (slotNumber == 0) {
//                        Method getSimID = telephonyClass.getMethod(methodName
//                                + SIM_variant, parameter);
//                        Object[] obParameter = new Object[1];
//                        obParameter[0] = slotNumber;
//                        ob_phone = getSimID.invoke(obj);
//                    }
//                }
//
//                if (ob_phone != null) {
//                    value = ob_phone.toString();
//                }
//            } catch (Exception e) {
//                logger.error("" + e);
//            }
//
//            return value;
//        }
//
//        public String invokeOldMethod(String className, int slotNumber,
//                                      String methodName, String SIM_variant) {
//            String val = "";
//            try {
//                Class<?> telephonyClass = Class
//                        .forName("android.telephony.TelephonyManager");
//                Constructor[] cons = telephonyClass.getDeclaredConstructors();
//                cons[0].getName();
//                cons[0].setAccessible(true);
//                Object obj = cons[0].newInstance();
//                Class<?>[] parameter = new Class[1];
//                parameter[0] = int.class;
//                Object ob_phone = null;
//                try {
//                    Method getSimID = telephonyClass.getMethod(methodName
//                            + SIM_variant, parameter);
//                    Object[] obParameter = new Object[1];
//                    obParameter[0] = slotNumber;
//                    ob_phone = getSimID.invoke(obj, obParameter);
//                } catch (Exception e) {
//                    if (slotNumber == 0) {
//                        Method getSimID = telephonyClass.getMethod(methodName
//                                + SIM_variant, parameter);
//                        Object[] obParameter = new Object[1];
//                        obParameter[0] = slotNumber;
//                        ob_phone = getSimID.invoke(obj);
//                    }
//                }
//
//                if (ob_phone != null) {
//                    val = ob_phone.toString();
//                }
//            } catch (Exception e) {
//                logger.error("" + e);
//            }
//            return val;
//        }
//    }
//
//    public final static String m_getDeviceId = "getDeviceId";
//
//    public final static String m_getSubscriberId = "getSubscriberId";
//
//    public final static String m_getSimSerialNumber = "getSimSerialNumber";
//
//    public static String telephonyClassName = "android.telephony.TelephonyManager";
////    public static String[] listofClass = new String[]{
////            "com.mediatek.telephony.TelephonyManagerEx",
////            "android.telephony.TelephonyManager",
////            "android.telephony.MSimTelephonyManager",
////            "com.android.internal.telephony.Phone",
////            "com.android.internal.telephony.PhoneFactory",
////            "com.lge.telephony.msim.LGMSimTelephonyManager",
////            "com.asus.telephony.AsusTelephonyManager",
////            "com.htc.telephony.HtcTelephonyManager"};
//
//    public static String[] listofClass = new String[]{
//            "com.mediatek.telephony.TelephonyManagerEx",
//            "android.telephony.MSimTelephonyManager",
//            "com.lge.telephony.msim.LGMSimTelephonyManager",
//            "com.asus.telephony.AsusTelephonyManager",
//            "com.htc.telephony.HtcTelephonyManager"};
//
//    private static String SIM_VARINT = "";
//    public static Class sTelephonyClass;
//    public static String FIELD_SLOT_NAME_0;
//    public static String FIELD_SLOT_NAME_1;
//
//    public static int getSlotNumber() {
//        try {
//            Field fields1 = sTelephonyClass.getDeclaredField(FIELD_SLOT_NAME_0);
//            fields1.setAccessible(true);
//            return (Integer) fields1.get(null);
//        } catch (Exception e) {
//            return 0;
//        }
//    }
//
//    public static int getSlotNumber_1() {
//        try {
//            Field fields1 = sTelephonyClass.getDeclaredField(FIELD_SLOT_NAME_1);
//            fields1.setAccessible(true);
//            return (Integer) fields1.get(null);
//        } catch (Exception e) {
//            return 1;
//        }
//    }
//
//    private static Class checkTelephonyClass() {
//        for (String className : listofClass) {
//            try {
//                Class telephonyClass = Class.forName(className);
//                return telephonyClass;
//            } catch (ClassNotFoundException e) {
//                logger.error("" + e);
//            }
//        }
//
//        return null;
//    }
//
//    public void init() {
//        sTelephonyClass = checkTelephonyClass();
//    }
//}
