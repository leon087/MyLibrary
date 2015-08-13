package cm.java.util;


import android.test.InstrumentationTestCase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidateUtilTest extends InstrumentationTestCase {

    /**
     * 判断8-15位字母或数据的合法密码
     */
    public static void testCheckPassword() throws Exception {
        String temp = "cdkjd198y198";
        boolean result = ValidateUtil.checkPassword(temp);
        assertEquals(result, true);
    }

    /**
     * 判断是否为移动手机号
     */
    public static void testCheckMobilePhone() throws Exception {
        String temp = "15700195027";
        boolean result = ValidateUtil.checkMobilePhone(temp);
        assertEquals(result, true);
    }

    /**
     * 判断是否为移动手机号
     */
    public static void testCheckMobilePhone2() throws Exception {
        String temp = "13200195027";
        boolean result = ValidateUtil.checkMobilePhone(temp);
        assertEquals(result, false);
    }

    public static void testIsValidate() throws Exception {
        String[] temp = {"13700195027", ""};
        boolean result = ValidateUtil.isValidate(temp);
        assertEquals(result, false);
    }

    public static void testIsValidate2() throws Exception {
        String[] temp = {"13700195027"};
        boolean result = ValidateUtil.isValidate(temp);
        assertEquals(result, true);
    }

    public static void testIsValidPostalCode() throws Exception {
        String temp = "111111";
        boolean result = ValidateUtil.isValidPostalCode(temp);
        assertEquals(result, true);
    }
}
