package cm.java.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ValidateUtilTest {

    /**
     * 判断8-15位字母或数据的合法密码
     */
    @Test
    public void testCheckPassword() throws Exception {
        String temp = "cdkjd198y198";
        boolean result = ValidateUtil.checkPassword(temp);
        assertEquals(result, true);
    }

    /**
     * 判断是否为移动手机号
     */
    @Test
    public void testCheckMobilePhone() throws Exception {
        String temp = "15700195027";
        boolean result = ValidateUtil.checkMobilePhone(temp);
        assertEquals(result, true);

        String temp1 = "13200195027";
        boolean result1 = ValidateUtil.checkMobilePhone(temp1);
        assertEquals(result1, false);
    }

    @Test
    public void testIsValidate() throws Exception {
        String[] temp = {"13700195027", ""};
        boolean result = ValidateUtil.isValidate(temp);
        assertEquals(result, false);
    }

    @Test
    public void testIsValidPostalCode() throws Exception {
        String temp = "111111";
        boolean result = ValidateUtil.isValidPostalCode(temp);
        assertEquals(result, true);
    }
}
