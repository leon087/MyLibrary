package cm.java.util;

import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;

public class ReflectTest {

    @Test
    public void testMethodAndCall() throws Exception {
        ArrayList list = new ArrayList();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        String s = Reflect.bind(list).method("get", int.class).call(1);
        assertEquals(s, "bbb");
    }

    @Test
    public void testCall() throws Exception {
        ArrayList list = new ArrayList();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        int size = Reflect.bind(list).call("size");
        assertEquals(size, 3);
    }

    @Test
    public void testField() throws Exception {
        ArrayList list = new ArrayList();
        int size = Reflect.bind(list).field("size", 4).field("size");
        assertEquals(size, 4);
    }

    @Test
    public void testNewInstance() throws Exception {
        Reflect reflect = Reflect.load(ArrayList.class).newInstance();
        reflect.method("add", Object.class).call("aaa");

        String s = reflect.method("get", int.class).call(0);
        assertEquals(s, "aaa");
    }
}
