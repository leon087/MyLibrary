package cm.java.util;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ObjectUtilTest extends InstrumentationTestCase {

    public void testNewHashMap() throws Exception {
        Map<String, String> map = ObjectUtil.newHashMap();
        boolean result = map != null;
        assertEquals(true, result);
    }

    public void testNewLinkedList() throws Exception {
        List<String> map = ObjectUtil.newLinkedList();
        boolean result = map != null;
        assertEquals(true, result);
    }

    public void testNewHashSet() throws Exception {
        Set<String> map = ObjectUtil.newHashSet();
        boolean result = map != null;
        assertEquals(true, result);
    }

    public void testNewArrayList() throws Exception {
        ArrayList<String> map = ObjectUtil.newArrayList();
        boolean result = map != null;
        assertEquals(true, result);
    }

}

