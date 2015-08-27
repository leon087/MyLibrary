package cm.java.util;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapUtilTest extends InstrumentationTestCase {

    public void testGetString() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("123key", "456value");
        String result = MapUtil.getString(map, "123key");
        assertEquals(result, "456value");

        String result1 = MapUtil.getString(map, "2222");
        assertEquals(result1, "");
    }

    public void testGetBoolean() throws Exception {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("123key", true);
        boolean result = MapUtil.getBoolean(map, "123key");
        assertEquals(result, true);

        boolean result1 = MapUtil.getBoolean(map, "222222");
        assertEquals(result1, false);
    }

    public void testGetLong() throws Exception {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("123key", (long) 34567890);
        long result = MapUtil.getLong(map, "123key");
        assertEquals(result, 34567890l);

        long result1 = MapUtil.getLong(map, "22222");
        assertEquals(result1, -1);
    }

    public void testGetInt() throws Exception {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("123key", 34567890);
        int result = MapUtil.getInt(map, "123key");
        assertEquals(result, 34567890);

        int result1 = MapUtil.getInt(map, "222332");
        assertEquals(result1, -1);
    }

    public void testGetDouble() throws Exception {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("123key", 3.1415927d);
        Double result = MapUtil.getDouble(map, "123key");
        assertEquals(result, 3.1415927d);
    }

    public void testGetFloat() throws Exception {
        Map<String, Float> map = new HashMap<String, Float>();
        map.put("123key", 3.1415927f);
        Float result = MapUtil.getFloat(map, "123key");
        assertEquals(result, 3.1415927f);

        Float result1 = MapUtil.getFloat(map, "213");
        assertEquals(result1, -1);
    }

    public void testGetList() throws Exception {
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(122445);
        map.put("123key", list);
        List<Integer> result = MapUtil.getList(map, "123key");
        assertEquals(result, list);

        List<Integer> result1 = MapUtil.getList(map, "213");
        assertEquals(result1.isEmpty(), true);
    }
}
