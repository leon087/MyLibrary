package cm.android.common.image;

import com.nostra13.universalimageloader.cache.disc.impl.LimitedAgeDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.DiskCacheUtils;
import com.nostra13.universalimageloader.utils.MemoryCacheUtils;
import com.nostra13.universalimageloader.utils.StorageUtils;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import java.io.File;

import cm.android.util.Utils;

public class ImageManager {

    public static final int CLEAR_CACHE_ALL = 0x01;

    public static final int CLEAR_CACHE_MEMORY = CLEAR_CACHE_ALL + 1;

    public static final int CLEAR_CACHE_DISK = CLEAR_CACHE_ALL + 2;

    private static int maxImageWidthForMemoryCache = 0;

    private static int maxImageHeightForMemoryCache = 0;

    private static int resId;

    private ImageManager() {
    }

    public static final DisplayImageOptions.Builder getDefaultOptionsBuilder(
            int imageResOnLoading, int imageResForEmptyUri, int imageResOnFail) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
        // builder.postProcessor(postProcessor)
        // if (defRedIs != -1) {
        // 设置图片在下载期间显示的图片
        builder.showImageOnLoading(imageResOnLoading);
        // 设置图片Uri为空或是错误的时候显示的图片
        builder.showImageForEmptyUri(imageResForEmptyUri);
        // 设置图片加载/解码过程中错误时候显示的图片
        builder.showImageOnFail(imageResOnFail);
        // }
        // 设置下载的图片是否缓存在内存中
        builder.cacheInMemory(true);
        // 设置下载的图片是否缓存在SD卡中
        builder.cacheOnDisk(true);
        // builder.displayer(new RoundedBitmapDisplayer(20));
        // 设置图片的解码类型
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        // 设置图片的解码配置
        // builder.decodingOptions(android.graphics.BitmapFactory.Options
        // decodingOptions)
        // 设置图片以如何的编码方式显示
        // imageScaleType(ImageScaleType imageScaleType)

        // builder.displayer(displayer);
        return builder;
    }

    public static DisplayImageOptions getOptions(int defRedIs) {
        DisplayImageOptions.Builder builder = getDefaultOptionsBuilder(
                defRedIs, defRedIs, defRedIs);
        return builder.build();
    }

    public static void init(Context context, String cacheDirPath, int defResId) {
        resId = defResId;
        ImageLoaderConfiguration defaultConfig = getConfiguration(context, cacheDirPath);
        ImageLoader.getInstance().init(defaultConfig);
    }

    public static void deInit() {
        clearCache(CLEAR_CACHE_MEMORY);
        ImageLoader.getInstance().destroy();
    }

    private static ImageLoaderConfiguration getConfiguration(Context context, String cacheDirPath) {
        ImageLoaderConfiguration.Builder builder = getDefaultConfigurationBuilder(
                context, cacheDirPath);
        return builder.build();
    }

    public static ImageLoaderConfiguration.Builder getDefaultConfigurationBuilder(
            Context context, String cacheDirPath) {
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(
                context);
        // 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
        // builder.denyCacheImageMultipleSizesInMemory();
        builder.diskCacheFileCount(100);
        File cacheDir = null;
        File reserveCacheDir = StorageUtils.getIndividualCacheDirectory(context);
        if (Utils.isEmpty(cacheDirPath)) {
            cacheDir = reserveCacheDir;
        } else {
            cacheDir = new File(cacheDirPath);
        }
        // 缓存100M
        // builder.discCache(new TotalSizeLimitedDiscCache(cacheDir,
        // new Md5FileNameGenerator(), 100 * 1024 * 1024));
        // 缓存保存5天
        builder.diskCache(new LimitedAgeDiskCache(cacheDir, reserveCacheDir,
                new Md5FileNameGenerator(), 24 * 60 * 60 * 5));
        builder.tasksProcessingOrder(QueueProcessingType.LIFO);
        builder.threadPriority(Thread.NORM_PRIORITY);
        builder.threadPoolSize(3);
        // builder.defaultDisplayImageOptions(options);
        // builder.memoryCache(memoryCache);
        builder.memoryCacheExtraOptions(maxImageWidthForMemoryCache,
                maxImageHeightForMemoryCache);
        return builder;
    }

    /**
     * 清理缓存
     */
    public static void clearCache(int flag) {
        if (flag == CLEAR_CACHE_MEMORY) {
            ImageLoader.getInstance().clearMemoryCache();
        } else if (flag == CLEAR_CACHE_DISK) {
            ImageLoader.getInstance().clearDiskCache();
        } else {
            ImageLoader.getInstance().clearMemoryCache();
            ImageLoader.getInstance().clearDiskCache();
        }
    }

    /**
     * 清理指定缓存
     */
    public static void clearCache(String uri) {
        MemoryCacheUtils.removeFromCache(uri, ImageLoader.getInstance().getMemoryCache());
        DiskCacheUtils.removeFromCache(uri, ImageLoader.getInstance().getDiskCache());
    }

    public static void display(String uri, ImageView imageView, int defResId) {
        ImageLoader.getInstance().displayImage(uri, imageView, getOptions(defResId));
    }

    public static void display(String uri, ImageView imageView) {
        ImageLoader.getInstance().displayImage(uri, imageView, getOptions(resId));
    }

}
