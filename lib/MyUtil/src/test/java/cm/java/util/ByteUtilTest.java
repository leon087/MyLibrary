package cm.java.util;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ByteUtilTest {

    @Test
    public void testToLong() throws Exception {
        byte[] input = {11, 22, 33};
        long temp = ByteUtil.toLong(input, 0, 2);
        assertEquals(temp, 5643l);
    }

    @Test
    public void testToBigEndianLong() throws Exception {
        byte[] input = {11, 22, 33};
        long temp = ByteUtil.toBigEndianLong(input, 0, 2);
        assertEquals(temp, 2838l);
    }

    @Test
    public void testToDouble() throws Exception {
        byte[] input = {11, 22, 33};
        double temp = ByteUtil.toDouble(input, 0, 2);
        assertEquals(temp, 5643.0);
    }

    @Test
    public void testToShort() throws Exception {
        byte[] input = {11, 22, 33};
        short temp = ByteUtil.toShort(input, 0);
        assertEquals(temp, 5643);
    }

    @Test
    public void testToIntFromTwoBytes() throws Exception {
        byte[] input = {11, 22, 33};
        int temp = ByteUtil.toIntFromTwoBytes(input, 0);
        assertEquals(temp, 5643);
    }

    @Test
    public void testToBigEndianIntFromTwoBytes() throws Exception {
        byte[] input = {11, 22, 33};
        int temp = ByteUtil.toBigEndianIntFromTwoBytes(input, 0);
        assertEquals(temp, 2838);
    }

    @Test
    public void testToBigEndianShort() throws Exception {
        byte[] input = {11, 22, 33};
        short temp = ByteUtil.toBigEndianShort(input, 0);
        assertEquals(temp, 2838);
    }

    @Test
    public void testToBigEndianInteger() throws Exception {
        byte[] input = {11, 22, 33};
        int temp = ByteUtil.toBigEndianInteger(input, 0, 2);
        assertEquals(temp, 2838);
    }

    @Test
    public void testToInteger() throws Exception {
        byte[] input = {11, 22, 33};
        int temp = ByteUtil.toInteger(input, 0, 2);
        assertEquals(temp, 5643);
    }

    @Test
    public void testToFloat() throws Exception {
        byte[] input = {11, 22, 33, 111, 1, 3, 4, 2};
        float temp = ByteUtil.toFloat(input, 0);
        assertEquals(temp, 4.9853735E28f);
    }

    @Test
    public void testToFloatEx() throws Exception {
        byte[] input = {11, 22, 33, 111, 1, 3, 4, 2};
        float temp = ByteUtil.toFloatEx(input, 0);
        assertEquals(temp, 2.8914102E-32f);
    }

    @Test
    public void testToStringUntil() throws Exception {
        byte[] input = {11, 22, 33, 111, 1, 3, 4, 2};
        byte until = 1;
        String temp = ByteUtil.toStringUntil(input, 0, until);
        assertEquals(temp, "\u000B\u0016!o");
    }

    @Test
    public void testIndexStringDevidedBySpace() throws Exception {
        byte[] input = {11, 22, 33, 111, 1, 3, 4, 2};
        String temp = ByteUtil.indexStringDevidedBySpace(input, 0, 8, 0);
        assertEquals(temp, "\u000B\u0016!o\u0001\u0003\u0004\u0002");
    }
}
