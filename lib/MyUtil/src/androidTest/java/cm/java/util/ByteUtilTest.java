package cm.java.util;

import android.test.InstrumentationTestCase;

public class ByteUtilTest extends InstrumentationTestCase {

    public void testToLong() throws Exception {
        byte[] input = {11, 22, 33};
        long temp = ByteUtil.toLong(input, 0, 2);
        assertEquals(temp, 5643l);
    }

    public void testToBigEndianLong() throws Exception {
        byte[] input = {11, 22, 33};
        long temp = ByteUtil.toBigEndianLong(input, 0, 2);
        assertEquals(temp, 2838l);
    }

    public void testToDouble() throws Exception {
        byte[] input = {11, 22, 33};
        double temp = ByteUtil.toDouble(input, 0, 2);
        assertEquals(temp, 5643.0);
    }

    public void testToShort() throws Exception {
        byte[] input = {11, 22, 33};
        short temp = ByteUtil.toShort(input, 0);
        assertEquals(temp, 5643);
    }

    public void testToIntFromTwoBytes() throws Exception {
        byte[] input = {11, 22, 33};
        int temp = ByteUtil.toIntFromTwoBytes(input, 0);
        assertEquals(temp, 5643);
    }

    public void testToBigEndianIntFromTwoBytes() throws Exception {
        byte[] input = {11, 22, 33};
        int temp = ByteUtil.toBigEndianIntFromTwoBytes(input, 0);
        assertEquals(temp, 2838);
    }

    public void testToBigEndianShort() throws Exception {
        byte[] input = {11, 22, 33};
        short temp = ByteUtil.toBigEndianShort(input, 0);
        assertEquals(temp, 2838);
    }

    public void testToBigEndianInteger() throws Exception {
        byte[] input = {11, 22, 33};
        int temp = ByteUtil.toBigEndianInteger(input, 0, 2);
        assertEquals(temp, 2838);
    }

    public void testToInteger() throws Exception {
        byte[] input = {11, 22, 33};
        int temp = ByteUtil.toInteger(input, 0, 2);
        assertEquals(temp, 5643);
    }

    public void testToFloat() throws Exception {
        byte[] input = {11, 22, 33, 111, 1, 3, 4, 2};
        float temp = ByteUtil.toFloat(input, 0);
        assertEquals(temp, 4.9853735E28f);
    }

    public void testToFloatEx() throws Exception {
        byte[] input = {11, 22, 33, 111, 1, 3, 4, 2};
        float temp = ByteUtil.toFloatEx(input, 0);
        assertEquals(temp, 2.8914102E-32f);
    }
}
