package cm.java.util;

import org.apache.http.Header;

import android.test.InstrumentationTestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UtilsTest extends InstrumentationTestCase {

    public void testGetRandom() throws Exception {
        int result1 = Utils.getRandom(1000);
        int result2 = Utils.getRandom(1000);
        boolean result = result1 == result2;
        assertEquals(result, false);
    }

    public void testIsEmpty() throws Exception {
        boolean temp = Utils.isEmpty("");
        assertEquals(temp, true);

        boolean temp1 = Utils.isEmpty(" ");
        assertEquals(temp1, false);

        Map<Object, Object> map = new HashMap<>();
        boolean temp2 = Utils.isEmpty(map);
        assertEquals(true, temp2);

        Map<Object, Object> map2 = new HashMap<>();
        map2.put("sss", "sss");
        boolean temp3 = Utils.isEmpty(map2);
        assertEquals(false, temp3);

        List<Object> list = new ArrayList<>();
        boolean temp4 = Utils.isEmpty(list);
        assertEquals(true, temp4);

        List<Object> list2 = new ArrayList<>();
        list2.add("ssss");
        boolean temp5 = Utils.isEmpty(list2);
        assertEquals(false, temp5);
    }

    public void testLookupHost() throws Exception {
        int temp = Utils.lookupHost("10.0.0.172");
        assertEquals(temp, 0xAC00000A);
    }

    public void testEncodeURLAndDecodeURL() throws Exception {
        String temp = Utils.encodeURL("asdan", "sss");
        String temp2 = Utils.decodeURL(temp, "sss");
        assertEquals(temp2, "asdan");
    }

    public void testNewRandomUUID() throws Exception {
        String temp = Utils.newRandomUUID();
        String temp2 = Utils.newRandomUUID();
        boolean result = temp.equals(temp2);
        assertEquals(result, false);
    }

    public void testBuildShortClassTag() throws Exception {
        String temp = Utils.buildShortClassTag(UtilsTest.class);
        assertEquals(temp, "UtilsTest");
    }

    public void testGetString() throws Exception {
        byte[] data = {111, 22, 44};
        String temp = Utils.getString(data, "csjnks");
        assertEquals(temp, "");
    }

    public void testGenHeaderAndGenHeader() throws Exception {
        Map<String, String> map = new HashMap<>();
        map.put("123", "321");
        Header[] temp = Utils.genHeader(map);
        Map<String, String> temp1 = Utils.genHeaderMap(temp);
        String result = temp1.get("123");
        assertEquals(result, "321");
    }

    public void testReplaceBlank() throws Exception {
        String result = Utils.replaceBlank("s   h  o ");
        assertEquals(result, "sho");
    }

    public void testCloneObject() throws Exception {
        String result = (String) Utils.cloneObject("sho");
        assertEquals(result, "sho");
    }
}
