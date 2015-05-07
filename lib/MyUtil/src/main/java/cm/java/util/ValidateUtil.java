package cm.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单检验工具类
 */
public class ValidateUtil {

    private static final Logger logger = LoggerFactory.getLogger("util");

    // 仅支持英文数字下划线中划线@英文句号
    public static final String RULE_USERNAME = "[0-9a-zA-Z\\._\\-@]*";

    public static final String RULE_POSTALCODE_SIMPLE = "^[0-9]{6}$";

    public static final String RULE_MOBILE_NUMBER_SIMPLE = "^1[0-9]{10}$";

    private ValidateUtil() {
    }

    /**
     * 判断8-15位字母或数据的合法密码
     */
    public static boolean checkPassword(String passWord) {
        if (passWord == null) {
            return false;
        }
        logger.info("=============passWord is:" + passWord);
        Pattern pattern = Pattern.compile("^[a-z|A-Z|0-9]{8,25}$");
        Matcher matcher = pattern.matcher(passWord);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否为移动手机号
     */
    public static boolean checkMobilePhone(String phone) {
        /*
         * 匹配移动手机号 "^1(3[4-9]|5[012789]|8[78])\d{8}$"以代码为准
		 *
		 * 匹配电信手机号 "^18[09]\d{8}$"
		 *
		 * 匹配联通手机号"^1(3[0-2]|5[56]|8[56])\d{8}$"
		 *
		 * 匹配CDMA手机号 "^1[35]3\d{8}$"
		 */
        Pattern pattern = Pattern
                .compile("^1((3[5-9]|5[012789]|8[278])\\d{8})|(134[0-8]\\d{7})$");
        Matcher matcher = pattern.matcher(phone);

        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 判断各字段是否为null或者为""
     *
     * @param str 各字段组成的数组
     * @return 任意一个字段为null或者"",返回false，否则返回 true
     */
    public static boolean isValidate(String[] str) {
        for (String string : str) {
            if (string == null || "".equals(string.trim())) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断各参数是否为null或者为""
     *
     * @param str 各字段组成的数组
     * @return 任意一个字段为null或者"",返回false，否则返回 true
     */
    public static boolean isValidate(Long[] str) {
        for (Long string : str) {
            if (string == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断参数是否为null或者为""
     *
     * @return 任意一个字段为null或者"",返回false，否则返回 true
     */
    public static boolean isValidate(Long str) {
        if (str == null) {
            return false;
        }
        return true;
    }

    // public static boolean isChinaMobile(String mobile) {
    // mobile = trimMobile(mobile);
    // if (mobile == null || "".equals(mobile)) {
    // return false;
    // }
    // String chinaMoblieStarts = PortalConstants.conf
    // .getProperty("chinaMoblieStarts");
    // String[] startNums = chinaMoblieStarts.split(",");
    // for (int i = 0; i < startNums.length; i++) {
    // if (mobile.startsWith(startNums[i])) {
    // return true;
    // }
    // }
    // return false;
    // }

    /**
     * 判断参数是否为null或者为""
     *
     * @return 字段为null或者"",返回false，否则返回 true
     */
    public static boolean isValidate(String str) {
        if (str == null || "".equals(str.trim())) {
            return false;
        }
        return true;
    }

    /**
     * 手机号码验证
     *
     * @param mobileCode 被验证的手机号码
     * @return 返回正确的手机号码，否则返回null
     */
    public static String trimMobile(String mobileCode) {
        if (mobileCode != null) {
            mobileCode = mobileCode.trim();
        }
        String mobile = mobileCode;
        if (mobileCode == null || mobileCode.length() == 12
                || mobileCode.length() < 11 || mobileCode.length() > 14) {
            return "";
        }
        if (mobileCode.length() == 14) {
            if (!mobileCode.startsWith("+86")) {
                return "";
            } else {
                mobile = mobileCode.substring(3);
            }
        }
        if (mobileCode.length() == 13) {
            if (!mobileCode.startsWith("86")) {
                return "";
            } else {
                mobile = mobileCode.substring(2);
            }
        }
        // if (!mobile.startsWith("13") && !mobile.startsWith("15") &&
        // !mobile.startsWith("18"))
        // return null;
        try {
            Long.parseLong(mobile.trim());
        } catch (Exception e) {
            return "";
        }
        if (!mobile.startsWith("1")) {
            return "";
        }
        return mobile;
    }

    public static String addAdrToMobile(String mobileCode) {
        String firstcode = "86";
        if (mobileCode != null) {
            mobileCode = mobileCode.trim();
        }
        String mobile = mobileCode;
        if (mobileCode == null || mobileCode.length() == 12
                || mobileCode.length() < 11 || mobileCode.length() > 14
                || mobileCode.length() == 0) {
            return "";
        }
        if (mobileCode.length() == 14) {
            if (!mobileCode.startsWith("+86")) {
                return "";
            } else {
                mobile = mobileCode.substring(3);
            }
        }
        if (mobileCode.length() == 13) {
            if (!mobileCode.startsWith("86")) {
                return "";
            } else {
                mobile = mobileCode.substring(2);
            }
        }
        try {
            Long.parseLong(mobile.trim());
        } catch (Exception e) {
            return "";
        }
        if (!mobile.startsWith("1")) {
            return "";
        }
        return firstcode + mobile;
    }

    // public static boolean isEmail(String email) {
    // String str =
    // "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
    // Pattern p = Pattern.compile(str);
    // Matcher m = p.matcher(email);
    // return m.matches();
    // }

    /**
     * 判断各字段是否为数字或者为字母 ，长度
     *
     * @param str 需要判断的字符串
     * @return 字段是数字或者为字母长度符合规则, 返回true，否则返回 false
     */
    public static boolean isChannelId(String str) {

        if (str != null && !"".equals(str)) {
            // 如果是默认的channelId则返回成功
            if (str.equals("0_10010001001")) {
                return true;
            }

            String strs[] = str.split("_");
            if (strs.length == 3 || strs.length == 2) {
                for (int i = 0; i < strs.length; i++) {
                    // 判断各字段是否为数字或者为字母
                    if (!isChar(strs[i], i)) {
                        return false;
                    }
                }
            } else if (strs.length == 1) {
                if (strs[0].length() != 11 && strs[0].length() != 4) {
                    return false;
                }

                for (int i = 0; i < strs[0].length(); i++) {
                    if (!(strs[0].charAt(i) >= '0' && strs[0].charAt(i) <= '9')) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * 判断各字段是否为数字或者为字母
     *
     * @param str 需要判断的字符串
     * @return 字段是数字或者为字母长度符合规则, 返回true，否则返回 false
     */
    private static boolean isChar(String str, int index) {
        // 判断WAP1为0 ，WAP2为1001
        if (index == 0) {
            if (str.length() == 4) {
                for (int i = 0; i < str.length(); i++) {
                    if (!(str.charAt(i) >= '0' && str.charAt(i) <= '9')) {
                        return false;
                    }
                }
                return true;
            }

            if (str.equals("0")) {
                return true;
            } else {
                return false;
            }
        }
        // 判断11位渠道ID为数字
        if (index == 1) {
            // 如果长度不为11 ，则不合法
            if (str.length() != 11) {
                return false;
            } else {// 如果标识不为数字 ，则不合法
                for (int i = 0; i < str.length(); i++) {
                    if (!(str.charAt(i) >= '0' && str.charAt(i) <= '9')) {
                        return false;
                    }
                }
            }

        }
        // 判断6位标识为数字
        else if (index == 2) {
            // 如果长度不为6 ，则不合法
            if (str.length() != 6) {
                return false;
            } else {// 如果标识不为数字 ，则不合法
                for (int i = 0; i < str.length(); i++) {
                    if (!(str.charAt(i) >= '0' && str.charAt(i) <= '9')) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    public static boolean isEmail(String email) {
        String str
                = "^([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\\_|\\.]?)*[a-zA-Z0-9]+\\.[a-zA-Z]{2,3}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 简单校验手机号（规则：长度为11，首位为1，仅数字）
     */
    public static boolean isValidMobileNumber(String mobile) {
        return isValidString(mobile, RULE_MOBILE_NUMBER_SIMPLE);
    }

    /**
     * 简单校验邮政编码（规则：长度为6，仅数字）
     */
    public static boolean isValidPostalCode(String postalCode) {
        return isValidString(postalCode, RULE_POSTALCODE_SIMPLE);
    }

    public static boolean isValidString(String str, String rule) {
        if (str == null) {
            return false;
        }

        if (!str.matches(rule)) {
            return false;
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println("123II_io-.@".matches(RULE_USERNAME));
        System.out.println("12312312311".matches(RULE_MOBILE_NUMBER_SIMPLE));
        System.out.println("223331".matches(RULE_POSTALCODE_SIMPLE));

        System.out.println(isValidMobileNumber("12312312311"));
        System.out.println(isValidPostalCode("223331"));
    }
}
