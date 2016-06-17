package cm.java.util;

import org.junit.Test;

import java.lang.reflect.Type;

import static junit.framework.Assert.assertEquals;

public class ReflectUtilTest {

    @Test
    public void testGetSuperClassGenricType() throws Exception {
        Type type = ReflectUtil.getSuperClassGenricType(HexUtilTest.class);
        assertEquals(type, Object.class);
    }
}
