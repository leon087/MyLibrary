package cm.android.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.StatFs;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * IO读写Util类
 */
public class IoUtil {
    private IoUtil() {
    }

    private static final int BUF_SIZE = 1024 * 100;

    /**
     * 判断目录是否可用, 已经挂载并且拥有可读可写权限 true 可用
     *
     * @param path
     * @return
     */
    public static boolean isDirectoryValid(String path) {
        File file = new File(path);
        if (!file.canWrite()) {
            return false;
        }
        StatFs sf = new StatFs(file.getPath());
        long availCount = sf.getAvailableBlocks();
        if (availCount > 0) {
            return true;
        }
        return false;
    }

    /**
     * 创建文件夹
     *
     * @param directorFilePath
     * @return
     */
    public static boolean createDirector(String directorFilePath) {
        String[] dirStructor = parseDirSturctor(directorFilePath);
        File file;
        boolean isSucceed = true;
        for (int i = 0; i < dirStructor.length; i++) {
            file = new File(dirStructor[i]);
            if (!file.exists()) {
                isSucceed = (isSucceed & file.mkdir());
                if (i == dirStructor.length - 1) {
                    return isSucceed;
                }
            }
        }
        return false;
    }

    /**
     * 移动资源配置文件至制定路径
     */
    public static void writeResToPhone(Context context, String fileName, int res) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (isFileExist(fileName)) {
                return;
            }
            createFile(fileName);

            inputStream = new BufferedInputStream(context.getResources()
                    .openRawResource(res));
            outputStream = new BufferedOutputStream(new FileOutputStream(
                    fileName));
            write(inputStream, outputStream);
        } catch (IOException e) {
            MyLog.e("fileName = " + fileName, e);
        } finally {
            closeQuietly(inputStream);
            closeQuietly(outputStream);
        }
    }

    /**
     * 移动APK文件至制定路径
     */
    public static void writeApkToPhone(Context context, String fileName, int res) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            if (isFileExist(fileName)) {
                return;
            }
            createFile(fileName);

            inputStream = new BufferedInputStream(context.getResources()
                    .openRawResource(res));
            outputStream = new BufferedOutputStream(context.openFileOutput(
                    fileName, Context.MODE_WORLD_READABLE
                            | Context.MODE_WORLD_WRITEABLE));
            write(inputStream, outputStream);
        } catch (IOException e) {
            MyLog.e("fileName = " + fileName, e);
        } finally {
            closeQuietly(inputStream);
            closeQuietly(outputStream);
        }
    }

    /**
     * 移动Assets目录下的文件
     *
     * @param context
     * @param assetPath
     * @param dir
     */
    public static void writeAssetToPhone(Context context, String assetPath,
                                         String dir) {
        if (getAllFiles(dir).length != 0) {
            return;
        }

        AssetManager assetManager = context.getAssets();
        if (!dir.endsWith(File.separator)) {
            dir += File.separator;
        }
        String[] files = null;
        try {
            files = assetManager.list(assetPath);
        } catch (IOException e) {
            MyLog.e("assetPath = " + assetPath, e);
            return;
        }

        for (String strSvy : files) {
            String filePath = assetPath + File.separator + strSvy;
            InputStream inputStream = null;
            OutputStream outputStream = null;
            try {
                inputStream = new BufferedInputStream(
                        assetManager.open(filePath));
                outputStream = new BufferedOutputStream(new FileOutputStream(
                        dir + strSvy));
                write(inputStream, outputStream);
            } catch (IOException e) {
                // e.printStackTrace();
                MyLog.e("assetPath = " + assetPath, e);
            } finally {
                closeQuietly(inputStream);
                closeQuietly(outputStream);
            }
        }
    }

    /**
     * Closes 'closeable', ignoring any checked exceptions. Does nothing if
     * 'closeable' is null.
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
                // e.printStackTrace();
                MyLog.e(e);
            }
        }
    }

    /**
     * 创建文件,fileName为文件全路径加文件名
     */
    public static boolean createFile(String fileName) {
        String[] dirStructor = parseDirSturctor(fileName);
        File file;
        for (int i = 0; i < dirStructor.length; i++) {
            file = new File(dirStructor[i]);
            if (i == dirStructor.length - 1) {
                if (!file.isFile()) {
                    return createNewFile(file);
                }
            } else {
                if (!file.exists()) {
                    file.mkdir();
                }
            }
        }
        return false;
    }

    /**
     * 删除文件
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除某个目录下的所有文件，但不删除该目录
     *
     * @param dir
     * @return
     */
    public static boolean deleteFiles(File dir) {
        if (!dir.isDirectory()) {
            return dir.delete();
        }

        boolean isSucceed = true;
        for (File file : dir.listFiles()) {
            isSucceed &= deleteFiles(file);
        }
        return isSucceed;
    }

    /**
     * 删除文件，如果该文件是个目录，则会删除该目录以及该目录下所有文件
     *
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        boolean isSucceed = deleteFiles(dir);
        isSucceed &= dir.delete();
        return isSucceed;
    }

    /**
     * 拷贝用户目录下文件到指定路径
     *
     * @param context
     * @param srcName      The name of the file to open; can not contain path separators.
     * @param destFilePath 拷贝路径
     */
    public static void copyFile(Context context, String srcName,
                                String destFilePath) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(
                    context.openFileInput(srcName));
            outputStream = new BufferedOutputStream(new FileOutputStream(
                    destFilePath));
            write(inputStream, outputStream);
        } catch (IOException e) {
            MyLog.e("srcName = " + srcName + ",destFilePath = " + destFilePath,
                    e);
        } finally {
            closeQuietly(inputStream);
            closeQuietly(outputStream);
        }
    }

    /**
     * 拷贝文件
     *
     * @param srcPath  源文件路径
     * @param destPath 拷贝后路径
     */
    public static void copyFile(String srcPath, String destPath) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(srcPath));
            outputStream = new BufferedOutputStream(new FileOutputStream(
                    destPath));
            write(inputStream, outputStream);
        } catch (IOException e) {
            MyLog.e("srcPath = " + srcPath + ",destPath = " + destPath, e);
        } finally {
            closeQuietly(inputStream);
            closeQuietly(outputStream);
        }
    }

    /**
     * 文件是否存在
     */
    public static boolean isFileExist(String filePath) {
        if (null == filePath) {
            return false;
        }

        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        return false;
    }

    /**
     * 创建文件
     *
     * @param file
     * @return
     */
    private static boolean createNewFile(File file) {
        try {
            return file.createNewFile();
        } catch (IOException e) {
            MyLog.e("file:" + file, e);
            return false;
        }
    }

    /**
     * @param path
     * @param postfix
     * @return
     */
    public static ArrayList<String> searchFileToString(String path,
                                                       String postfix) {
        if (!path.endsWith(File.separator)) {
            path = path + File.separator;
        }

        ArrayList<String> fileToString = new ArrayList<String>();
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            // 不是一个目录
            return fileToString;
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                String name = files[i].getName();
                // String stuffer = name.substring( name.lastIndexOf( "." ) );
                if (name.endsWith(postfix)) {
                    fileToString.add(path + files[i].getName());
                }
            }
        }
        return fileToString;
    }

    /**
     * @param remotePathName
     * @return
     */
    private static String[] parseDirSturctor(String remotePathName) {
        ArrayList<String> dirList = new ArrayList<String>();
        String path = remotePathName;
        int index = 0;
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == File.separatorChar && i > 0) {
                dirList.add(path.substring(0, i));
                index = i;
            }
        }
        if (index < path.length()) {
            dirList.add(path.substring(0, path.length()));
        }
        String[] retStr = new String[dirList.size()];
        return (String[]) dirList.toArray(retStr);
    }

    /**
     * 获取指定路径下的所有文件列表
     *
     * @param remotePathName
     * @return
     */
    public static String[] getAllFiles(String remotePathName) {
        File file = new File(remotePathName);
        if (!file.isDirectory())
            return new String[0];
        return file.list();
    }

    /**
     * 获取指定路径下的所有文件
     *
     * @param
     * @return
     */
    public static File[] getFiles(File dir, FilenameFilter filenameFilter) {
        if (!dir.isDirectory())
            return new File[0];
        return dir.listFiles(filenameFilter);
    }

    /**
     * 获取执行路径下的所有文件列表，按最后修改时间排序
     *
     * @param remotePathName
     * @return
     */
    public static List<String> getAllFilesByLastModifyTime(String remotePathName) {
        List<String> allFilesName = new ArrayList<String>();
        File file = new File(remotePathName);
        if (!file.isDirectory())
            return new ArrayList<String>();
        File[] fileList = file.listFiles();
        // 按照文件最后修改时间排序
        // List<File> infoIds = Arrays.asList( file.listFiles() );
        Arrays.sort(fileList, new Comparator<File>() {
            public int compare(File o1, File o2) {
                return ((int) (o1.lastModified() - o2.lastModified()));
            }
        });

        for (File f : fileList) {
            if (f.getAbsoluteFile().isFile()) {
                allFilesName.add(f.getName());
            }
        }
        return allFilesName;
    }

    /**
     * 获取指定目录下文件大小
     *
     * @param rootDir
     * @return
     */
    public static long getDirTotalSize(String rootDir) {
        File dir = new File(rootDir);

        File[] files = dir.listFiles();
        if (null == files) {
            return 0;
        }
        long chrFileLen = 0;
        for (File file : files) {
            chrFileLen += file.length();
        }

        return chrFileLen;
    }

    /**
     * 根据文件路径获取文件名称
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (null == filePath)
            return null;
        int end = filePath.lastIndexOf(".");
        int start = filePath.lastIndexOf(File.separator);
        String fileName = filePath.substring(start + 1, end);
        return fileName;
    }

    /**
     * 文件重命名
     *
     * @param oldPath 原来的文件名
     * @param newPath 新文件名
     */
    public static boolean renameFile(String oldPath, String newPath) {
        if (!oldPath.equals(newPath)) {// 新的文件名和以前文件名不同时,才有必要进行重命名
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            // 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
            if (!newfile.exists()) {
                return oldfile.renameTo(newfile);
            } else {
                MyLog.e("newfile.exists() = true");
            }
        }
        return false;
    }

    /**
     * 读取源文件
     *
     * @throws Exception
     */
    public static byte[] readFile(File file) {
        if (null == file || !file.isFile()) {
            return null;
        }

        ByteArrayOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file));
            outputStream = new ByteArrayOutputStream(BUF_SIZE);

            write(inputStream, outputStream);
            byte[] bytes = outputStream.toByteArray();
            return bytes;
        } catch (IOException e) {
            MyLog.e("file = " + file, e);
            return null;
        } finally {
            closeQuietly(outputStream);
            closeQuietly(inputStream);
        }
    }

    /**
     * 将byte数组写入文件
     *
     * @param value
     * @param file
     * @return
     */
    public static boolean writeFile(byte[] value, File file) {
        if (null == value || null == file) {
            return false;
        }

        if (file.isDirectory()) {
            return false;
        }

        file.delete();
        boolean createFile = createFile(file.getAbsolutePath());
        if (!createFile) {
            return false;
        }

        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(value);
            outputStream.flush();
            return true;
        } catch (IOException e) {
            MyLog.e("file = " + file, e);
            return false;
        } finally {
            closeQuietly(outputStream);
        }
    }

    /**
     * 把srcFilePath写入/data/data/<包名>/files/目录下
     *
     * @param srcFilePath
     * @param destFileName
     * @param context
     * @return
     */
    public static boolean writeToDataDir(String srcFilePath,
                                         String destFileName, Context context) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(
                    srcFilePath));
            outputStream = new BufferedOutputStream(context.openFileOutput(
                    destFileName, Context.MODE_WORLD_READABLE
                            | Context.MODE_WORLD_WRITEABLE));
            write(inputStream, outputStream);
            return true;
        } catch (Exception e) {
            MyLog.e("srcFilePath = " + srcFilePath + ",destFileName = "
                    + destFileName, e);
            return false;
        } finally {
            closeQuietly(inputStream);
            closeQuietly(outputStream);
        }
    }

    /**
     * 读取{@link zipfile}中{@link entryName}文件写入到{@link soFile}中
     *
     * @param zipfile   zip压缩包
     * @param entryName 需要读取的压缩包中文件文件名
     * @param soFile
     * @return
     */
    public static boolean writeZipFile(ZipFile zipfile, String entryName,
                                       File soFile) {
        InputStream is = null;
        OutputStream os = null;
        try {
            ZipEntry zipentry = zipfile.getEntry(entryName);
            if (!soFile.exists() || zipentry.getSize() != soFile.length()) {
                is = new BufferedInputStream(zipfile.getInputStream(zipentry));
                os = new BufferedOutputStream(new FileOutputStream(soFile));
                write(is, os);
            } else {
                // log.i("JNI library " + soname.getAbsolutePath()
                // + " is up to date");
            }
            return true;
        } catch (Exception e) { // I am still lazy ~~~
            MyLog.e("zipfile = " + zipfile + ",entryName = " + entryName
                    + ",soFile = " + soFile, e);
            return false;
        } finally {
            closeQuietly(is);
            closeQuietly(os);
        }
    }

    /**
     * 将{@link inputStream}中内容写入{@link outputStream}
     *
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void write(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        int count = -1;
        byte[] buffer = new byte[BUF_SIZE];
        while ((count = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, count);
        }
    }

    /**
     * 读取{@link inputStream}中内容，以byte[]返回
     *
     * @param inputStream
     * @return
     */
    public static byte[] read(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            write(inputStream, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            MyLog.e(e);
            return null;
        } finally {
            closeQuietly(baos);
        }
    }

    /**
     * 对象序列化
     *
     * @param obj
     * @return
     */
    public static byte[] serialIn(Object obj) {
        if (obj == null) {
            return null;
        }
        ByteArrayOutputStream baos = null;
        ObjectOutputStream oos = null;
        try {
            baos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            return baos.toByteArray();
        } catch (Exception e) {
            MyLog.e(e);
            return null;
        } finally {
            IoUtil.closeQuietly(baos);
            IoUtil.closeQuietly(oos);
        }
    }

    /**
     * 返序列化
     *
     * @param buf
     * @return
     */
    public static Object serialOut(byte[] buf) {
        if (buf == null) {
            return null;
        }
        ByteArrayInputStream baos = null;
        ObjectInputStream ois = null;
        try {
            baos = new ByteArrayInputStream(buf);
            ois = new ObjectInputStream(baos);
            Object o = ois.readObject();
            return o;
        } catch (Exception e) {
            MyLog.e(e);
            return null;
        } finally {
            IoUtil.closeQuietly(baos);
            IoUtil.closeQuietly(ois);
        }
    }
}