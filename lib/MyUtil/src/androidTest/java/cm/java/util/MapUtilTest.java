package cm.java.util;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapUtilTest extends InstrumentationTestCase {

    public void testGetString1() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("123key", "456value");
        String result = MapUtil.getString(map, "123key");
        assertEquals(result, "456value");
    }

    public void testGetString2() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
//        map.put("123key", "456value");
        String result = MapUtil.getString(map, "123key");
        assertEquals(result, "");
    }

    public void testGetBoolean() throws Exception {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("123key", true);
        boolean result = MapUtil.getBoolean(map, "123key");
        assertEquals(result, true);
    }

    public void testGetBoolean2() throws Exception {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
//        map.put("123key", true);
        boolean result = MapUtil.getBoolean(map, "123key");
        assertEquals(result, false);
    }

    public void testGetLong() throws Exception {
        Map<String, Long> map = new HashMap<String, Long>();
        map.put("123key", (long) 34567890);
        long result = MapUtil.getLong(map, "123key");
        assertEquals(result, (long) 34567890);
    }

    public void testGetLong2() throws Exception {
        Map<String, Long> map = new HashMap<String, Long>();
//        map.put("123key", (long) 34567890);
        long result = MapUtil.getLong(map, "123key");
        assertEquals(result, (long) -1);
    }

    public void testGetInt() throws Exception {
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("123key", 34567890);
        int result = MapUtil.getInt(map, "123key");
        assertEquals(result, 34567890);
    }

    public void testGetInt2() throws Exception {
        Map<String, Integer> map = new HashMap<String, Integer>();
//        map.put("123key", 34567890);
        int result = MapUtil.getInt(map, "123key");
        assertEquals(result, -1);
    }

    public void testGetDouble() throws Exception {
        Map<String, Double> map = new HashMap<String, Double>();
        map.put("123key", 3.1415927);
        Double result = MapUtil.getDouble(map, "123key");
        assertEquals(result, 3.1415927);
    }

    public void testGetFloat() throws Exception {
        Map<String, Float> map = new HashMap<String, Float>();
        map.put("123key", (float) 3.1415927);
        Float result = MapUtil.getFloat(map, "123key");
        assertEquals(result, (float) 3.1415927);
    }

    public void testGetFloat2() throws Exception {
        Map<String, Float> map = new HashMap<String, Float>();
//        map.put("123key", (float) 3.1415927);
        Float result = MapUtil.getFloat(map, "123key");
        assertEquals(result, (float) -1);
    }

    public void testGetList() throws Exception {
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        List<Integer> list = new ArrayList<Integer>();
        list.add(122445);
        map.put("123key", list);
        List<Integer> result = MapUtil.getList(map, "123key");
        assertEquals(result, list);
    }

    public void testGetList2() throws Exception {
        Map<String, List<Integer>> map = new HashMap<String, List<Integer>>();
        List<Integer> list = new ArrayList<Integer>();
//        list.add(122445);
//        map.put("123key", list);
        List<Integer> result = MapUtil.getList(map, "123key");
        assertEquals(result.isEmpty(), true);
    }

}
