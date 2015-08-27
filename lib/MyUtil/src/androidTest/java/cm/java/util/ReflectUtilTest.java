package cm.java.util;

import android.test.InstrumentationTestCase;

import java.lang.reflect.Type;

public class ReflectUtilTest extends InstrumentationTestCase {

    public void testGetSuperClassGenricType() throws Exception {
        Type type = ReflectUtil.getSuperClassGenricType(HexUtilTest.class);
        assertEquals(type, Object.class);
    }
}
