package cm.java.util;

import android.test.InstrumentationTestCase;

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

        String temp1 = "13200195027";
        boolean result1 = ValidateUtil.checkMobilePhone(temp1);
        assertEquals(result1, false);
    }

    public static void testIsValidate() throws Exception {
        String[] temp = {"13700195027", ""};
        boolean result = ValidateUtil.isValidate(temp);
        assertEquals(result, false);

        boolean result1 = ValidateUtil.isValidate(temp);
        assertEquals(result1, true);
    }

    public static void testIsValidPostalCode() throws Exception {
        String temp = "111111";
        boolean result = ValidateUtil.isValidPostalCode(temp);
        assertEquals(result, true);
    }
}
