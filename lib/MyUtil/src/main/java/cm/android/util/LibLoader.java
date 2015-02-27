package cm.android.util;

import android.annotation.TargetApi;
import android.content.Context;

import java.io.File;
import java.net.URL;
import java.util.zip.ZipFile;

import cm.java.util.IoUtil;

/**
 * so文件加载器
 */
public class LibLoader {

    /**
     * 拷贝so文件并加载至内存
     */
    @TargetApi(8)
    public static void load(Context context, String soFileName, String entryName) {
        File sopath = context.getDir("libs", Context.MODE_PRIVATE);
        File soname = new File(sopath, soFileName);
        try {
            sopath.mkdirs();
            File zip = new File(context.getPackageCodePath());
            ZipFile zipfile = new ZipFile(zip);
            boolean result = IoUtil.writeZipFile(zipfile, entryName,
                    soname);
            if (result) {
                System.load(soname.getAbsolutePath());
            }
            // System.load(LIBRARY_NAME);
            // setHyphenationMethod(HYPH_NONE, new byte[] {});
        } catch (Exception e) {
            // log.e("cannot install " + LIBRARY_NAME + " library", e);
        }
    }

    /**
     * 拷贝so文件并加载至内存
     *
     * @deprecated
     */
    @Deprecated
    public static void load(Class klass, String libFileName) {
        try {
            URL location = klass.getProtectionDomain().getCodeSource()
                    .getLocation();
            ZipFile zf = new ZipFile(location.getPath());
            // libhellojni.so is put in the lib folder

            String entryName = "libs/" + libFileName;
            File soname = File.createTempFile("JNI-", "Temp");
            boolean result = IoUtil.writeZipFile(zf, entryName, soname);
            if (result) {
                System.load(soname.getAbsolutePath());
            }
            soname.delete();
        } catch (Exception e) { // I am still lazy ~~~
            e.printStackTrace();
        }
    }
}
