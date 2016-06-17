package cm.android.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class BitmapUtilTest {

//    @Test
//    public void testRotateBitmap() throws Exception {
//        Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
//        Bitmap temp1 = BitmapUtil.rotateBitmap(bitmap, 90);
//        Bitmap temp2 = BitmapUtil.rotateBitmap(bitmap, -270);
//        byte[] result1 = bitmap2Bytes(temp1);
//        byte[] result2 = bitmap2Bytes(temp2);
//        assertEquals(new String(result1), new String(result2));
//    }

    @Test
    public void testResizeBitmap() throws Exception {
        Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        Bitmap temp1 = BitmapUtil.resizeBitmap(bitmap, 1.3f);
        Bitmap temp2 = BitmapUtil.resizeBitmap(bitmap, 1.3f);
        byte[] result1 = bitmap2Bytes(temp1);
        byte[] result2 = bitmap2Bytes(temp2);
        assertEquals(new String(result1), new String(result2));
    }

    @Test
    public void testReverseBitmap() throws Exception {
        Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        Bitmap temp1 = BitmapUtil.reverseBitmap(bitmap, 1);
        Bitmap temp2 = BitmapUtil.reverseBitmap(bitmap, 1);

        assertEquals(temp1 != null, true);

        byte[] result1 = bitmap2Bytes(temp1);
        byte[] result2 = bitmap2Bytes(temp2);
        assertEquals(new String(result1), new String(result2));
    }

//    @Test
//    public void testAddBigFrame() throws Exception {
//        Context context = TestUtil.getContext();
//        Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
//        Bitmap temp1 = BitmapUtil.addBigFrame(context, bitmap, 1);
//        Bitmap temp2 = BitmapUtil.addBigFrame(context, bitmap, 1);
//
//        assertEquals(temp1 != null, true);
//
//        byte[] result1 = bitmap2Bytes(temp1);
//        byte[] result2 = bitmap2Bytes(temp2);
//        assertEquals(new String(result1), new String(result2));
//    }

    @Test
    public void testCreateBitmapWithReflection() throws Exception {
        Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        Bitmap temp1 = BitmapUtil.createBitmapWithReflection(bitmap, 1, 1);
        Bitmap temp2 = BitmapUtil.createBitmapWithReflection(bitmap, 1, 1);

        assertEquals(temp1 != null, true);

        byte[] result1 = bitmap2Bytes(temp1);
        byte[] result2 = bitmap2Bytes(temp2);
        assertEquals(new String(result1), new String(result2));
    }

    @Test
    public void testCreateReflectionBitmapWithGradient() throws Exception {
        Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        Bitmap temp1 = BitmapUtil.createReflectionBitmapWithGradient(bitmap, 1);
        Bitmap temp2 = BitmapUtil.createReflectionBitmapWithGradient(bitmap, 1);

        assertEquals(temp1 != null, true);

        byte[] result1 = bitmap2Bytes(temp1);
        byte[] result2 = bitmap2Bytes(temp2);
        assertEquals(new String(result1), new String(result2));
    }

    @Test
    public void testCreateReflectionBitmap() throws Exception {
        Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        Bitmap temp1 = BitmapUtil.createReflectionBitmap(bitmap, 1);
        Bitmap temp2 = BitmapUtil.createReflectionBitmap(bitmap, 1);

        assertEquals(temp1 != null, true);

        byte[] result1 = bitmap2Bytes(temp1);
        byte[] result2 = bitmap2Bytes(temp2);
        assertEquals(new String(result1), new String(result2));
    }

    @Test
    public void testCreateLinearGradient() throws Exception {
        Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        Bitmap temp1 = BitmapUtil.createLinearGradient(bitmap);
        Bitmap temp2 = BitmapUtil.createLinearGradient(bitmap);

        assertEquals(temp1 != null, true);

        byte[] result1 = bitmap2Bytes(temp1);
        byte[] result2 = bitmap2Bytes(temp2);
        assertEquals(new String(result1), new String(result2));
    }

    @Test
    public void testCreateRotateBitmap() throws Exception {
        Bitmap bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888);
        Bitmap temp1 = BitmapUtil.createRotateBitmap(bitmap, 180f);
        Bitmap temp2 = BitmapUtil.createRotateBitmap(bitmap, -180f);

        assertEquals(temp1 != null, true);

        byte[] result1 = bitmap2Bytes(temp1);
        byte[] result2 = bitmap2Bytes(temp2);
        assertEquals(new String(result1), new String(result2));
    }

    private byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
