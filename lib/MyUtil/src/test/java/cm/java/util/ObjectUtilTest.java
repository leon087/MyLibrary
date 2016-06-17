package cm.java.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static junit.framework.Assert.assertEquals;

public class ObjectUtilTest {

    @Test
    public void testNewHashMap() throws Exception {
        Map<String, String> map = ObjectUtil.newHashMap();
        boolean result = map != null;
        assertEquals(true, result);
    }

    @Test
    public void testNewLinkedList() throws Exception {
        List<String> map = ObjectUtil.newLinkedList();
        boolean result = map != null;
        assertEquals(true, result);
    }

    @Test
    public void testNewHashSet() throws Exception {
        Set<String> map = ObjectUtil.newHashSet();
        boolean result = map != null;
        assertEquals(true, result);
    }

    @Test
    public void testNewArrayList() throws Exception {
        ArrayList<String> map = ObjectUtil.newArrayList();
        boolean result = map != null;
        assertEquals(true, result);
    }

}

