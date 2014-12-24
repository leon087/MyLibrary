package cm.android.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.view.View;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class BitmapUtil {

    private static final Logger logger = LoggerFactory.getLogger(BitmapUtil.class);

    private BitmapUtil() {
    }

    /**
     * 图片旋转
     *
     * @param bmp    要旋转的图片
     * @param degree 图片旋转的角度，负值为逆时针旋转，正值为顺时针旋转
     */
    public static Bitmap rotateBitmap(Bitmap bmp, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(),
                matrix, true);
    }

    /**
     * 图片缩放
     *
     * @param scale 值小于则为缩小，否则为放大
     */
    public static Bitmap resizeBitmap(Bitmap bm, float scale) {
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(),
                matrix, true);
    }

    /**
     * 图片反转
     *
     * @param flag 0为水平反转，1为垂直反转
     */
    public static Bitmap reverseBitmap(Bitmap bmp, int flag) {
        float[] floats = null;
        switch (flag) {
            case 0: // 水平反转
                floats = new float[]{-1f, 0f, 0f, 0f, 1f, 0f, 0f, 0f, 1f};
                break;
            case 1: // 垂直反转
                floats = new float[]{1f, 0f, 0f, 0f, -1f, 0f, 0f, 0f, 1f};
                break;
        }

        if (floats != null) {
            Matrix matrix = new Matrix();
            matrix.setValues(floats);
            return Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                    bmp.getHeight(), matrix, true);
        }

        return null;
    }

    /**
     * 添加边框
     *
     * @param bm  原图片
     * @param res 边框资源
     */
    public static Bitmap addBigFrame(Context context, Bitmap bm, int res) {
        Bitmap bitmap = decodeBitmap(context, res);
        Drawable[] array = new Drawable[2];
        array[0] = new BitmapDrawable(bm);
        Bitmap b = zoomBitmap(bitmap, bm.getWidth(), bm.getHeight());
        array[1] = new BitmapDrawable(b);
        LayerDrawable layer = new LayerDrawable(array);
        return drawableToBitmap(layer);
    }

    /**
     * 将Drawable转换成Bitmap
     */
    private static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        // canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    /**
     * 图片与边框组合
     *
     * @param bm  原图片
     * @param res 边框资源
     */
    private static Bitmap combinateFrame(Context context, Bitmap bm, int[] res) {
        Bitmap bmp = decodeBitmap(context, res[0]);
        // 边框的宽高
        final int smallW = bmp.getWidth();
        final int smallH = bmp.getHeight();

        // 原图片的宽高
        final int bigW = bm.getWidth();
        final int bigH = bm.getHeight();

        int wCount = (int) Math.ceil(bigW * 1.0 / smallW);
        int hCount = (int) Math.ceil(bigH * 1.0 / smallH);

        // 组合后图片的宽高
        int newW = (wCount + 2) * smallW;
        int newH = (hCount + 2) * smallH;

        // 重新定义大小
        Bitmap newBitmap = Bitmap.createBitmap(newW, newH, Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint p = new Paint();
        p.setColor(Color.TRANSPARENT);
        canvas.drawRect(new Rect(0, 0, newW, newH), p);

        Rect rect = new Rect(smallW, smallH, newW - smallW, newH - smallH);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        canvas.drawRect(rect, paint);

        // 绘原图
        canvas.drawBitmap(bm, (newW - bigW - 2 * smallW) / 2 + smallW, (newH
                - bigH - 2 * smallH)
                / 2 + smallH, null);
        // 绘边框
        // 绘四个角
        int startW = newW - smallW;
        int startH = newH - smallH;
        Bitmap leftTopBm = decodeBitmap(context, res[0]); // 左上角
        Bitmap leftBottomBm = decodeBitmap(context, res[2]); // 左下角
        Bitmap rightBottomBm = decodeBitmap(context, res[4]); // 右下角
        Bitmap rightTopBm = decodeBitmap(context, res[6]); // 右上角

        canvas.drawBitmap(leftTopBm, 0, 0, null);
        canvas.drawBitmap(leftBottomBm, 0, startH, null);
        canvas.drawBitmap(rightBottomBm, startW, startH, null);
        canvas.drawBitmap(rightTopBm, startW, 0, null);

        leftTopBm.recycle();
        leftTopBm = null;
        leftBottomBm.recycle();
        leftBottomBm = null;
        rightBottomBm.recycle();
        rightBottomBm = null;
        rightTopBm.recycle();
        rightTopBm = null;

        // 绘左右边框
        Bitmap leftBm = decodeBitmap(context, res[1]);
        Bitmap rightBm = decodeBitmap(context, res[5]);
        for (int i = 0, length = hCount; i < length; i++) {
            int h = smallH * (i + 1);
            canvas.drawBitmap(leftBm, 0, h, null);
            canvas.drawBitmap(rightBm, startW, h, null);
        }

        leftBm.recycle();
        leftBm = null;
        rightBm.recycle();
        rightBm = null;

        // 绘上下边框
        Bitmap bottomBm = decodeBitmap(context, res[3]);
        Bitmap topBm = decodeBitmap(context, res[7]);
        for (int i = 0, length = wCount; i < length; i++) {
            int w = smallW * (i + 1);
            canvas.drawBitmap(bottomBm, w, startH, null);
            canvas.drawBitmap(topBm, w, 0, null);
        }

        bottomBm.recycle();
        bottomBm = null;
        topBm.recycle();
        topBm = null;

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return newBitmap;
    }

    /**
     * 将图片内容解析成字节数组
     *
     * @return byte[]
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        byte[] data = outStream.toByteArray();
        outStream.close();
        inStream.close();
        return data;
    }

    /**
     * 将字节数组转换Bitmap
     *
     * @return Bitmap
     */
    public static Bitmap getPicFromBytes(byte[] bytes,
            BitmapFactory.Options opts) {
        if (bytes != null) {
            if (opts != null) {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            } else {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }
        return null;
    }

    /**
     * 图片缩放
     *
     * @param bitmap 对象
     * @param w      要缩放的宽度
     * @param h      要缩放的高度
     * @return newBmp 新 Bitmap对象
     */
    public static Bitmap zoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newBmp = Bitmap.createBitmap(bitmap, 0, 0, width, height,
                matrix, true);
        return newBmp;
    }

    /**
     * 把Bitmap转Byte
     */
    public static byte[] bitmapToBytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 根据File返回Bitmap
     */
    public static Bitmap decodeBitmap(File imgFile, int displayWidth,
            int displayHeight) throws OutOfMemoryError {
        if (imgFile == null) {
            return null;
        }
        if (!imgFile.isFile()) {
            return null;
        }

        Bitmap bitmap = null;
        if (displayWidth == 0 || displayHeight == 0) {
            bitmap = getBitmap(imgFile);
        } else {
            // 图片过大进行解码图片
            bitmap = decodeBitmap(imgFile.getAbsolutePath(), displayWidth,
                    displayHeight);
        }
        return bitmap;
    }

    private static Bitmap getBitmap(File imgFile) throws OutOfMemoryError {
        InputStream is = null;
        try {
            // is = new BufferedInputStream(new FileInputStream(path));
            // bmp = BitmapFactory.decodeStream(is, null, op);
            is = new FileInputStream(imgFile);
            return BitmapFactory.decodeFileDescriptor(
                    ((FileInputStream) is).getFD(), null, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IoUtil.closeQuietly(is);
        }
    }

    /**
     * 解析图片
     */
    public static Bitmap decodeBitmap(String path, int displayWidth,
            int displayHeight) throws OutOfMemoryError {
        BitmapFactory.Options op = getOptions(path, displayWidth, displayHeight);
        InputStream is = null;
        try {
            // is = new BufferedInputStream(new FileInputStream(path));
            // bmp = BitmapFactory.decodeStream(is, null, op);
            is = new FileInputStream(path);
            return BitmapFactory.decodeFileDescriptor(
                    ((FileInputStream) is).getFD(), null, op);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            IoUtil.closeQuietly(is);
        }
    }

    // /**
    // * 读取Assets资源Drawable/AssetManager
    // */
    // public static Drawable getAssertDrawable(Context context, String path) {
    // return new BitmapDrawable(readAssetsBitmap(context, path));
    // }

    /**
     * 读取Assets资源Drawable/AssetManager
     */
    public static Bitmap readAssetsBitmap(Context context, String path,
            int displayWidth, int displayHeight) throws OutOfMemoryError {
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            is = context.getAssets().open(path);
            // bitmap = readBitmap(is);
            bitmap = decodeBitmap(is, displayWidth, displayHeight);
        } catch (IOException e) {
            logger.error("", e);
            return null;
        } finally {
            IoUtil.closeQuietly(is);
        }
        return bitmap;
    }

    /**
     * 解析assets目录下文件
     */
    public static Bitmap readAssetsBitmap(Context context, String path)
            throws OutOfMemoryError {
        return readAssetsBitmap(context, path, 0, 0);
    }

    /**
     * 解析图片（待测试）
     */
    public static Bitmap decodeBitmap(InputStream is, int displayWidth,
            int displayHeight) throws OutOfMemoryError {
        BitmapFactory.Options op = getOptions(is, displayWidth, displayHeight);
        return BitmapFactory.decodeStream(is, null, op);
    }

    /**
     * 以最省内存的方式读取本地资源的图片
     */
    public static Bitmap decodeBitmap(Context context, int resId)
            throws OutOfMemoryError {
        InputStream is = context.getResources().openRawResource(resId);
        try {
            Bitmap bitmap = readBitmap(is);
            return bitmap;
        } catch (OutOfMemoryError e) {
            throw e;
        } finally {
            IoUtil.closeQuietly(is);
        }
    }

    private static Bitmap readBitmap(InputStream is) throws OutOfMemoryError {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        // opt.inJustDecodeBounds = true;// 只描边，不读取数据
        opt.inInputShareable = true;
        // 获取资源图片
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, opt);
        return bitmap;
    }

    /**
     * 获得设置信息
     */
    public static BitmapFactory.Options getOptions(InputStream is,
            int displayWidth, int displayHeight) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;// 只读取Bitmap的宽高等信息，不读取像素。
        BitmapFactory.decodeStream(is, null, op); // 获取尺寸信息
        // 获取比例大小
        // Rect r = new Rect(0, 0, displayWidth, displayHeight);
        // int w = r.width();
        // int h = r.height();
        // int maxSize = w > h ? w : h;
        // int inSimpleSize = computeSampleSize(op, UNCONSTRAINED, w * h);
        if (displayWidth != 0 && displayHeight != 0) {
            int inSimpleSize = calculateInSampleSize(op, displayWidth,
                    displayHeight);
            op.inSampleSize = inSimpleSize;
            // op.inSamplySize 表示的是缩小的比例
            // op.inSamplySize = 4,表示缩小1/4的宽和高，1/16的像素，android认为设置为2是最快的。
        }
        // op.outWidth表示的是图像真实的宽度
        op.inJustDecodeBounds = false;

        // 用于节省内存
        op.inPreferredConfig = Bitmap.Config.RGB_565;
        op.inPurgeable = true;
        op.inInputShareable = true;
        return op;
    }

    /**
     * 获得设置信息
     */
    public static BitmapFactory.Options getOptions(String path,
            int displayWidth, int displayHeight) {
        BitmapFactory.Options op = new BitmapFactory.Options();
        op.inJustDecodeBounds = true;// 只读取Bitmap的宽高等信息，不读取像素。
        BitmapFactory.decodeFile(path, op); // 获取尺寸信息
        // 获取比例大小
        // Rect r = new Rect(0, 0, displayWidth, displayHeight);
        // int w = r.width();
        // int h = r.height();
        // int maxSize = w > h ? w : h;
        // int inSimpleSize = computeSampleSize(op, UNCONSTRAINED, w * h);
        if (displayWidth != 0 && displayHeight != 0) {
            int inSimpleSize = calculateInSampleSize(op, displayWidth,
                    displayHeight);
            op.inSampleSize = inSimpleSize;
            // op.inSamplySize 表示的是缩小的比例
            // op.inSamplySize = 4,表示缩小1/4的宽和高，1/16的像素，android认为设置为2是最快的。
        }
        // op.outWidth表示的是图像真实的宽度
        op.inJustDecodeBounds = false;

        // 用于节省内存
        op.inPreferredConfig = Bitmap.Config.RGB_565;
        op.inPurgeable = true;
        op.inInputShareable = true;
        return op;
    }

    // /*
    // * Compute the sample size as a function of minSideLength and
    // * maxNumOfPixels. minSideLength is used to specify that minimal width or
    // * height of a bitmap. maxNumOfPixels is used to specify the maximal size
    // in
    // * pixels that is tolerable in terms of memory usage.
    // *
    // * The function returns a sample size based on the constraints. Both size
    // * and minSideLength can be passed in as IImage.UNCONSTRAINED, which
    // * indicates no care of the corresponding constraint. The functions
    // prefers
    // * returning a sample size that generates a smaller bitmap, unless
    // * minSideLength = IImage.UNCONSTRAINED.
    // *
    // * Also, the function rounds up the sample size to a power of 2 or
    // multiple
    // * of 8 because BitmapFactory only honors sample size this way. For
    // example,
    // * BitmapFactory downsamples an image by 2 even though the request is 3.
    // So
    // * we round up the sample size to avoid OOM.
    // */
    // public static int computeSampleSize(BitmapFactory.Options options,
    // int minSideLength, int maxNumOfPixels) {
    // int initialSize = computeInitialSampleSize(options, minSideLength,
    // maxNumOfPixels);
    //
    // int roundedSize;
    // if (initialSize <= 8) {
    // roundedSize = 1;
    // while (roundedSize < initialSize) {
    // roundedSize <<= 1;
    // }
    // } else {
    // roundedSize = (initialSize + 7) / 8 * 8;
    // }
    // return roundedSize;
    // }
    //
    // public static final int UNCONSTRAINED = -1;
    //
    // public static int computeInitialSampleSize(BitmapFactory.Options options,
    // int minSideLength, int maxNumOfPixels) {
    // double w = options.outWidth;
    // double h = options.outHeight;
    // int lowerBound = (maxNumOfPixels == UNCONSTRAINED) ? 1 : (int) Math
    // .ceil(Math.sqrt(w * h / maxNumOfPixels));
    // int upperBound = (minSideLength == UNCONSTRAINED) ? 128 : (int) Math
    // .min(Math.floor(w / minSideLength),
    // Math.floor(h / minSideLength));
    //
    // if (upperBound < lowerBound) {
    // // return the larger one when there is no overlapping zone.
    // return lowerBound;
    // }
    // if ((maxNumOfPixels == UNCONSTRAINED)
    // && (minSideLength == UNCONSTRAINED)) {
    // return 1;
    // } else if (minSideLength == UNCONSTRAINED) {
    // return lowerBound;
    // } else {
    // return upperBound;
    // }
    // }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options}
     * object when decoding bitmaps using the decode* methods from
     * {@link BitmapFactory}. This implementation calculates the closest
     * inSampleSize that will result in the final decoded bitmap having a width
     * and height equal to or larger than the requested width and height. This
     * implementation does not ensure a power of 2 is returned for inSampleSize
     * which can be faster when decoding but results in a larger bitmap which
     * isn't as useful for caching purposes.
     *
     * @param options   An options object with out* params already populated (run
     *                  through a decode* method with inJustDecodeBounds==true
     * @param reqWidth  The requested width of the resulting bitmap
     * @param reqHeight The requested height of the resulting bitmap
     * @return The value to be used for inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
            int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int width = options.outWidth;
        final int height = options.outHeight;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee a final image
            // with both dimensions larger than or equal to the requested height
            // and width.
            // TODO 2013-07-25 ggg，在参数宽高比例与原图不一致的情况下，按最小的那个比例缩放
            inSampleSize = heightRatio > widthRatio ? heightRatio : widthRatio;

            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger
            // inSampleSize).

            final float totalPixels = width * height;

            // Anything more than 2x the requested pixels we'll sample down
            // further
            final float totalReqPixelsCap = reqWidth * reqHeight * 2;

            while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
                inSampleSize++;
            }
        }
        return inSampleSize;
    }

    /**
     * 创建带有倒影效果的bitmap（canvas中直接画上原bitmap和倒影图片）
     *
     * @param reflectionHeight 倒影高
     * @param reflectionGap    原图与倒影之间的间距
     */
    public static Bitmap createBitmapWithReflection2(Bitmap srcBitmap,
            int reflectionHeight, int reflectionGap) {
        // 原始宽高
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        // 倒影高
        // int reflectionHeight = srcHeight / 4;
        // 创建倒影图片，图片反转+渐变
        Bitmap reflectionBitmap = createReflectionBitmapWithGradient(srcBitmap,
                reflectionHeight);

        // 倒影与原图的间隔
        final int REFLECTION_GAP = reflectionGap;
        // 最终的bitmap
        Bitmap finalBitmapWithReflection = Bitmap.createBitmap(srcWidth,
                srcHeight + reflectionHeight + REFLECTION_GAP,
                Bitmap.Config.ARGB_8888);
        // finalBitmapWithReflection.eraseColor(Color.TRANSPARENT);

        // Prepare the canvas to draw stuff.
        Canvas canvas = new Canvas(finalBitmapWithReflection);
        // Draw the original bitmap.
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        // Draw the reflection bitmap.
        canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);

        return finalBitmapWithReflection;
    }

    /**
     * 创建带有渐变效果的bitmap（canva中先画好原bitmap和反转bitmap后再处理渐变），推荐使用
     */
    public static Bitmap createBitmapWithReflection(Bitmap srcBitmap,
            int reflectionHeight, int reflectionGap) {
        // 原始宽高
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        // 倒影高
        // int reflectionHeight = srcHeight / 4;
        // 创建倒影图片，图片反转+渐变
        Bitmap reflectionBitmap = createReflectionBitmap(srcBitmap,
                reflectionHeight);

        // 倒影与原图的间隔
        final int REFLECTION_GAP = reflectionGap;
        // 最终的bitmap
        Bitmap finalBitmapWithReflection = Bitmap.createBitmap(srcWidth,
                srcHeight + reflectionHeight + REFLECTION_GAP,
                Bitmap.Config.ARGB_8888);
        // finalBitmapWithReflection.eraseColor(Color.TRANSPARENT);

        // Prepare the canvas to draw stuff.
        Canvas canvas = new Canvas(finalBitmapWithReflection);
        // Draw the original bitmap.
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        // Draw the reflection bitmap.
        canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP, null);

        // 渐变在最后实现
        processLinearGradient(canvas, srcHeight + reflectionGap, srcWidth,
                finalBitmapWithReflection.getHeight());

        return finalBitmapWithReflection;
    }

    /**
     * 创建倒影+渐变图片
     *
     * @param reflectionHeight 倒影高
     */
    public static Bitmap createReflectionBitmapWithGradient(Bitmap srcBitmap,
            int reflectionHeight) {
        Bitmap reflectionBitmap = createReflectionBitmap(srcBitmap,
                reflectionHeight);
        return createLinearGradient(reflectionBitmap);
    }

    /**
     * 创建倒影图片（无渐变）
     */
    public static Bitmap createReflectionBitmap(Bitmap srcBitmap,
            int reflectionHeight) {
        // 原始宽高
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        // 倒影高
        // int reflectionHeight = srcHeight / 4;
        // 实现图片反转
        Matrix matrix = new Matrix();
        // 1表示放大比例，不放大也不缩小。 -1表示在y轴上相反，即旋转180度。
        matrix.preScale(1, -1);
        // 创建反转后的图片Bitmap对象
        Bitmap reflectionBitmap = Bitmap.createBitmap(srcBitmap, 0, srcHeight
                - reflectionHeight, srcWidth, reflectionHeight, matrix, false);
        return reflectionBitmap;
    }

    /**
     * 创建渐变图像
     */
    public static Bitmap createLinearGradient(Bitmap srcBitmap) {
        Bitmap finalBitmap = Bitmap.createBitmap(srcBitmap.getWidth(),
                srcBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        processLinearGradient(canvas, 0, srcBitmap.getWidth(),
                srcBitmap.getHeight());
        return finalBitmap;
    }

    /**
     * 竖直方向渐变
     */
    public static void processLinearGradient(Canvas canvas, float top,
            float width, float bottom) {
        /**
         * 渐变<br>
         * 参数一：为渐变起点坐标的x轴位置.<br>
         * 参数二 ：为渐变起点坐标的y轴位置. <br>
         * 参数三：为渐变终点坐标的x轴位置，<br>
         * 参数四：为渐变终点坐标的y轴位置，<br>
         */
        // LinearGradient shader = new LinearGradient(0, 0, 0,
        // reflectionBitmap.getHeight(), 0x70FFFFFF, 0x00FFFFFF,
        // TileMode.CLAMP);
        LinearGradient shader = new LinearGradient(0, top, 0, bottom,
                0x70FFFFFF, 0x00FFFFFF, TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);
        // Set the Transfer mode to be porter duff and destination in
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // Draw the linear shader.
        canvas.drawRect(0, top, width, bottom, paint);
    }

    /**
     * 创建沿Y轴旋转的bitmap
     */
    public static Bitmap createRotateBitmap(Bitmap srcBitmap, float angle) {
        // 旋转角度
        double angleRadians = Math.toRadians(angle);
        int width = (int) (Math.cos(angleRadians) * srcBitmap.getWidth());

        Matrix matrix = new Matrix();
        // // 对称轴翻转
        Camera camera = new Camera();
        camera.save();
        // rotate
        // camera.rotateX(angle);
        camera.rotateY(angle);
        // camera.rotateZ(rotateZ);
        // translate
        // camera.translate(0, 0, translateZ);
        camera.getMatrix(matrix);
        // 恢复到之前的初始状态。
        camera.restore();
        // 设置图像处理的中心点
        matrix.preTranslate(srcBitmap.getWidth() >> 1,
                srcBitmap.getHeight() >> 1);
        matrix.preSkew(0f, 0.1f);
        // matrix.postSkew(skewX, skewY);
        // 直接setSkew()，则前面处理的rotate()、translate()等等都将无效。

        Bitmap bitmap = Bitmap.createBitmap(srcBitmap, 0, 0,
                srcBitmap.getWidth(), srcBitmap.getHeight(), matrix, true);
        return bitmap;
    }

    /**
     * 从view 得到图片
     */
    @TargetApi(4)
    public static Bitmap getBitmapFromView(View view) {
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED), View.MeasureSpec
                .makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);
        return bitmap;

        // int spec = View.MeasureSpec.makeMeasureSpec(0,
        // View.MeasureSpec.UNSPECIFIED);
        // view.measure(spec, spec);
        // view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        // Bitmap b = Bitmap.createBitmap(view.getWidth(), view.getHeight(),
        // Bitmap.Config.ARGB_8888);
        // Canvas canvas = new Canvas(bitmap);
        // view.draw(canvas);
        // return b;
    }
}
