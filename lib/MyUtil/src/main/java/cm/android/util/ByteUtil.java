package cm.android.util;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

/**
 * 将二进制流转为其他数据类型
 */
public class ByteUtil {

    /**
     * TENS[i] contains the tens digit of the number i, 0 <= i <= 99.
     */
    private static final char[] TENS = {'0', '0', '0', '0', '0', '0', '0',
            '0', '0', '0', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1',
            '2', '2', '2', '2', '2', '2', '2', '2', '2', '2', '3', '3', '3',
            '3', '3', '3', '3', '3', '3', '3', '4', '4', '4', '4', '4', '4',
            '4', '4', '4', '4', '5', '5', '5', '5', '5', '5', '5', '5', '5',
            '5', '6', '6', '6', '6', '6', '6', '6', '6', '6', '6', '7', '7',
            '7', '7', '7', '7', '7', '7', '7', '7', '8', '8', '8', '8', '8',
            '8', '8', '8', '8', '8', '9', '9', '9', '9', '9', '9', '9', '9',
            '9', '9'};

    /**
     * Ones [i] contains the tens digit of the number i, 0 <= i <= 99.
     */
    private static final char[] ONES = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2',
            '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7', '8',
            '9', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9', '0', '1', '2', '3', '4',
            '5', '6', '7', '8', '9', '0', '1', '2', '3', '4', '5', '6', '7',
            '8', '9',};

    /**
     * Table for MOD / DIV 10 computation described in Section 10-21 of Hank
     * Warren's "Hacker's Delight" online addendum.
     * http://www.hackersdelight.org/divcMore.pdf
     */
    private static final char[] MOD_10_TABLE = {0, 1, 2, 2, 3, 3, 4, 5, 5, 6,
            7, 7, 8, 8, 9, 0};

    private static final char SPACE_CH = ' ';

    /**
     * 私有构造函数，不允许实例化该类
     */
    private ByteUtil() {
    }

    /**
     * 将byte流转换为long
     *
     * @param byte[] b 二进制流
     * @param short  pos 开始转换位置
     * @return long 转换后的值
     */
    public static long toLong(byte[] b, int pos) {
        return toLong(b, pos, 8);
    }

    /**
     * 将byte流转换为long
     *
     * @param byte[] b 二进制流
     * @param int    pos 开始转换位置
     * @param int    width 转换字节数
     * @return long 转换后的值
     */
    public static long toLong(byte[] b, int pos, int width) {
        long ret = 0;
        for (int i = 0; i < width; i++) {
            ret |= (b[i + pos] & 0xFFl) << (8 * i);
        }
        return ret;
    }

    /**
     * 将byte流按照网络字节序转换为long
     *
     * @param byte[] b 二进制流
     * @param int    pos 开始转换位置
     * @param int    width 转换字节数
     * @return long 转换后的值
     */
    public static long toBigEndianLong(byte[] b, int pos, int width) {
        long ret = 0;
        for (int i = 0; i < width; i++) {
            ret |= (b[i + pos] & 0xFFl) << (8 * (width - i - 1));
        }
        return ret;
    }

    /**
     * 将byte流转换为double
     *
     * @param byte[] b 二进制流
     * @param short  pos 开始转换位置
     * @return double 转换后的值
     */
    public static double toDouble(byte[] b, int pos) {
        long l;

        l = b[pos];
        l &= 0xff;
        l |= ((long) b[pos + 1] << 8);
        l &= 0xffff;
        l |= ((long) b[pos + 2] << 16);
        l &= 0xffffff;
        l |= ((long) b[pos + 3] << 24);
        l &= 0xffffffffl;
        l |= ((long) b[pos + 4] << 32);
        l &= 0xffffffffffl;

        l |= ((long) b[pos + 5] << 40);
        l &= 0xffffffffffffl;
        l |= ((long) b[pos + 6] << 48);

        l |= ((long) b[pos + 7] << 56);
        return Double.longBitsToDouble(l);
    }

    /**
     * 将byte流转换为short
     *
     * @param byte[] b 二进制流
     * @param short  pos 开始转换位置
     * @return short 转换后的值
     */
    public static short toShort(byte[] b, int pos) {
        short ret = 0;
        ret |= (b[pos] & 0xFF);
        ret |= (b[pos + 1] & 0xFF) << 8;
        return ret;
    }

    /**
     * 将两个byte流转换为int
     *
     * @param byte[] b 二进制流
     * @param short  pos 开始转换位置
     * @return int 转换后的值
     */
    public static int toIntFromTwoBytes(byte[] b, int pos) {
        int ret = 0;
        ret |= (b[pos] & 0xFF);
        ret |= (b[pos + 1] & 0xFF) << 8;

        return (int) ret;
    }

    /**
     * 将两个byte流转换为int,大字端
     *
     * @param byte[] b 二进制流
     * @param short  pos 开始转换位置
     * @return int 转换后的值
     */
    public static int toBigEndianIntFromTwoBytes(byte[] b, int pos) {
        int ret = 0;
        ret |= (b[pos + 1] & 0xFF);
        ret |= (b[pos] & 0xFF) << 8;

        return (int) ret;
    }

    /**
     * 将byte流按照网络字节序转换为short
     *
     * @param byte[] b 二进制流
     * @param short  pos 开始转换位置
     * @return short 转换后的值
     */
    public static short toBigEndianShort(byte[] b, int pos) {
        short ret = 0;
        ret |= (b[pos] & 0xFF) << 8;
        ret |= (b[pos + 1] & 0xFF);
        return ret;
    }

    /**
     * 将byte流按照网络字节序转换为int
     *
     * @param byte[] b 二进制流
     * @param short  pos 开始转换位置
     * @return int 转换后的值
     */
    public static int toBigEndianInteger(byte[] b, int pos) {
        int ret = 0;
        for (int i = 0; i < 4; i++) {
            ret |= (b[i + pos] & 0xFF) << (8 * (3 - i));
        }
        return ret;
    }

    /**
     * 将int按网络字节流转换为byte数组
     *
     * @param int    val 要转换的源
     * @param byte[] b 目标数组
     * @param short  pos 开始存放位置下标
     */
    public static void toBigEndianByteArray(int val, byte[] b, int pos) {
        assert (pos + 4 <= b.length);
        for (int i = 3; i >= 0; i--) {
            b[pos + i] = (byte) (val & 0x000000FF);
            val = val >> 8;
        }
    }

    /**
     * 将short按网络字节序转换为byte数组
     *
     * @param short  val 要转换的源
     * @param byte[] b 目标数组
     * @param short  pos 开始存放位置下标
     */
    public static void toBigEndianByteArray(short val, byte[] b, int pos) {
        assert (pos + 2 <= b.length);
        b[pos + 1] = (byte) (val & 0x00FF);
        b[pos] = (byte) ((val & 0xFF00) >> 8);
    }

    /**
     * 将long按网络字节序转换为byte数组
     *
     * @param long   val 要转换的源
     * @param byte[] b 目标数组
     * @param short  pos 开始存放位置下标
     */
    public static void toBigEndianByteArray(long val, byte[] b, int pos) {
        assert (pos + 8 <= b.length);
        for (int i = 7; i >= 0; i--) {
            b[pos + i] = (byte) (val & 0x00000000000000FFl);
            val = val >> 8;
        }
    }

    /**
     * 将byte流转换为int
     *
     * @param byte[] b 二进制流
     * @param short  pos 开始转换位置
     * @return int 转换后的值
     */
    public static int toInteger(byte[] b, int pos) {
        int ret = 0;
        for (int i = 0; i < 4; i++) {
            ret |= (b[i + pos] & 0xFF) << (8 * i);
        }
        return ret;
    }

    /**
     * 将byte流转换为int
     *
     * @param byte[] b 二进制流
     * @param int    pos 开始转换位置
     * @param int    width 从几个byte转
     * @return int 转换后的值
     */
    public static double toDouble(byte[] b, int pos, int width) {
        double retVal = Double.MAX_VALUE;
        switch (width) {
            case 1:
            case 2:
            case 4:
                return (double) toInteger(b, pos, width);
            case 8:
                return toDouble(b, pos);
            default:
                break;
        }
        return retVal;
    }

    public static int toBigEndianInteger(byte[] b, int pos, int width) {
        int retVal = Integer.MAX_VALUE;
        switch (width) {
            case 1:
                retVal = b[pos];
                if (retVal < 0) {
                    retVal &= 0x000000FF;
                }
                break;
            case 2:
                retVal = toBigEndianIntFromTwoBytes(b, pos);
                break;
            case 4:
                retVal = toBigEndianInteger(b, pos);
                break;
            default:
                break;
        }

        return retVal;
    }

    public static int toInteger(byte[] b, int pos, int width) {
        int retVal = Integer.MAX_VALUE;
        switch (width) {
            case 1:
                retVal = b[pos];
                if (retVal < 0) {
                    retVal &= 0x000000FF;
                }
                break;
            case 2:
                retVal = toIntFromTwoBytes(b, pos);
                break;
            case 4:
                retVal = toInteger(b, pos);
                break;
            default:
                break;
        }

        return retVal;
    }

    /**
     * 将byte流转换为float
     *
     * @param byte[] b 二进制流
     * @param short  pos 开始转换位置
     * @return float 转换后的值
     */
    public static float toFloat(byte[] b, int pos) {
        int accum = 0;
        for (int i = 0; i < 4; i++) {
            accum |= (b[i + pos] & 0xff) << i * 8;
        }
        return Float.intBitsToFloat(accum);
    }

    public static float toFloatEx(byte[] b, int pos) {
        try {
            byte[] byteTmp = new byte[4];
            for (int i = 0; i < 4; i++) {
                byteTmp[i] = b[pos + i];
            }
            ByteBuffer bb = ByteBuffer.wrap(byteTmp);

            FloatBuffer fb = bb.asFloatBuffer();

            return fb.get();
        } catch (Exception e) {
            e.printStackTrace();
            return 0.0f;
        }

    }

    /**
     * 将float转为byte[]
     */
    public static void toByteArray(float val, byte[] b, int pos) {
        try {
            ByteBuffer bb = ByteBuffer.allocate(4);
            FloatBuffer fb = bb.asFloatBuffer();
            fb.put(val);

            for (int i = 0; i < 4; i++) {
                b[pos + i] = bb.get(i);
            }
            // b = bb.array();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将long转换为byte数组
     *
     * @param long   val 要转换的源
     * @param byte[] b 目标数组
     * @param short  pos 开始存放位置下标
     */
    public static void toByteArray(long val, byte[] b, int pos) {
        assert (pos + 8 <= b.length);
        for (int i = 0; i < 8; ++i) {
            b[pos + i] = (byte) (val & 0x00000000000000FFl);
            val = val >> 8;
        }
    }

    /**
     * 将long的指定长度字节转换为byte数组
     *
     * @param long   val 要转换的源 int w 要转化宽度
     * @param byte[] b 目标数组
     * @param short  pos 开始存放位置下标
     */
    public static void toByteArray(long val, int w, byte[] b, int pos) {
        assert (pos + 8 <= b.length);
        for (int i = 0; i < w; ++i) {
            b[pos + i] = (byte) (val & 0x00000000000000FFl);
            val = val >> 8;
        }
    }

    /**
     * 将int转换为byte数组
     *
     * @param int    val 要转换的源
     * @param byte[] b 目标数组
     * @param short  pos 开始存放位置下标
     */
    public static void toByteArray(int val, byte[] b, int pos) {
        assert (pos + 4 <= b.length);
        for (int i = 0; i < 4; ++i) {
            b[pos + i] = (byte) (val & 0x000000FF);
            val = val >> 8;
        }
    }

    /**
     * 将short转换为byte数组
     *
     * @param short  val 要转换的源
     * @param byte[] b 目标数组
     * @param short  pos 开始存放位置下标
     */
    public static void toByteArray(short val, byte[] b, int pos) {
        assert (pos + 2 <= b.length);
        b[pos] = (byte) (val & 0x00FF);
        b[pos + 1] = (byte) ((val & 0xFF00) >> 8);
    }

    /**
     * 将指定位置的字节数组转化为指定长度的字符串
     */
    public static String toStringWithLength(byte[] b, int pos, int length) {
        StringBuilder str = new StringBuilder();

        for (int i = 0; i < length; i++) {
            str.append((char) b[pos + i]);
        }

        String ret = str.toString();
        return ret;
    }

    /**
     * 返回一个字符串，从指定位置，直到指定的结束字符
     */
    public static String toStringUntil(byte[] b, int pos, byte until) {
        StringBuilder str = new StringBuilder();

        while (true) {
            if (b.length <= pos) {
                return null;
            }

            byte ch = b[pos];

            if (ch == until) {
                break;
            }
            str.append((char) ch);
            pos++;
        }

        String ret = str.toString();
        return ret;
    }

    /**
     * 从指定位置开始，返回指定的第N个字符串，字符串之间以空格作为间隔符 最后一个字符串可能以length处为结束 index：从0开始计数
     */
    public static String indexStringDevidedBySpace(byte[] b, int pos,
            int length, int index) {
        StringBuilder str = new StringBuilder();

        int spaceNum = 0;
        boolean valid = false;

        // 指定位置开始即为第一个字符串
        if (index == 0) {
            valid = true;
        }
        for (int i = 0; i < length; i++) {
            char ch = (char) b[pos + i];
            if (SPACE_CH == ch) {
                // 字符结束
                if (valid) {
                    break;
                }

                spaceNum++;
                // 字符开始
                if (spaceNum == index) {
                    valid = true;
                    continue;
                }
            }

            if (valid) {
                str.append(ch);
            }
        }

        String retStr = str.toString();
        return retStr;
    }
}
