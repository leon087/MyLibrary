package cm.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cm.java.cmd.CmdExecute;

/**
 * IO读写Util类
 */
public class IoUtil {

    private static final int BUF_SIZE = 8 * 1024;

    private static final Logger logger = LoggerFactory.getLogger(IoUtil.class);

    private IoUtil() {
    }

    /**
     * 创建文件夹
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
     * Closes 'closeable', ignoring any checked exceptions. Does nothing if
     * 'closeable' is null.
     */
    public static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (RuntimeException rethrown) {
            throw rethrown;
        } catch (Exception e) {
            // e.printStackTrace();
            logger.error("", e);
        }
    }

//    /**
//     * 创建文件,fileName为文件全路径加文件名
//     */
//    public static boolean createFile(String fileName) {
//        String[] dirStructor = parseDirSturctor(fileName);
//        File file;
//        for (int i = 0; i < dirStructor.length; i++) {
//            file = new File(dirStructor[i]);
//            if (i == dirStructor.length - 1) {
//                if (!file.isFile()) {
//                    return createNewFile(file);
//                }
//            } else {
//                if (!file.exists()) {
//                    file.mkdir();
//                }
//            }
//        }
//        return false;
//    }

    public static boolean createFile(File file) {
        File parent = file.getParentFile();
        if (checkDirectory(parent)) {
            try {
                file.createNewFile();
                return true;
            } catch (IOException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 删除某个目录下的所有文件，但不删除该目录
     */
    public static boolean deleteFiles(File dir) {
        if (!dir.isDirectory()) {
            logger.error("dir.isDirectory() = false");
            return false;
        }

        boolean isSucceed = true;
        for (File file : dir.listFiles()) {
            isSucceed &= deleteFile(file);
        }
        return isSucceed;
    }

    /**
     * 删除文件，如果该文件是个目录，则会删除该目录以及该目录下所有文件
     */
    public static boolean deleteFile(File file) {
        if (file == null || !file.exists()) {
            return true;
        }

        try {
            CmdExecute.exec("rm -fr " + file.getAbsolutePath());
            return true;
        } catch (Exception e) {
            try {
                return file.delete();
            } catch (SecurityException se) {
                logger.error("dir = " + file.getAbsolutePath(), e);
                return false;
            }
        }
    }

    /**
     * 拷贝文件
     *
     * @param srcFile  源文件
     * @param destFile 拷贝后文件
     */
    public static void copyFile(File srcFile, File destFile) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(srcFile));
            outputStream = new BufferedOutputStream(new FileOutputStream(destFile));
            write(inputStream, outputStream, BUF_SIZE);
        } catch (IOException e) {
            logger.error("srcFile = " + srcFile.getAbsolutePath() + ",destFile = " + destFile
                    .getAbsolutePath(), e);
        } finally {
            closeQuietly(inputStream);
            closeQuietly(outputStream);
        }
    }

    /**
     * 文件是否存在
     */
    public static boolean isFileExist(String filePath) {
        if (Utils.isEmpty(filePath)) {
            logger.error("filePath = " + filePath);
            return false;
        }

        File file = new File(filePath);
        if (file.exists()) {
            return true;
        }
        return false;
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
     */
    public static String[] getAllFiles(String remotePathName) {
        File file = new File(remotePathName);
        if (!file.isDirectory()) {
            return new String[0];
        }
        return file.list();
    }

    public static String[] getFiles(File fir) {
        if (!fir.isDirectory()) {
            return new String[0];
        }
        return fir.list();
    }

    /**
     * 获取指定路径下的所有文件
     */
    public static File[] getFiles(File dir, FilenameFilter filenameFilter) {
        if (!dir.isDirectory()) {
            return new File[0];
        }
        return dir.listFiles(filenameFilter);
    }

    /**
     * 获取执行路径下的所有文件列表，按最后修改时间排序
     */
    public static List<String> getAllFilesByLastModifyTime(String remotePathName) {
        List<String> allFilesName = new ArrayList<String>();
        File file = new File(remotePathName);
        if (!file.isDirectory()) {
            return new ArrayList<String>();
        }
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
     */
    public static long getDirTotalSize(File rootDir) {
        File[] files = rootDir.listFiles();
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
     * 文件重命名
     */
    public static boolean renameFile(File oldFile, File newFile) {
        if (oldFile.equals(newFile)) {
            return true;
        }

        // 若在该目录下已经有一个文件和新文件名相同，则不允许重命名
        if (!newFile.exists()) {
            return oldFile.renameTo(newFile);
        } else {
            logger.error("newFile.exists() = true");
            return false;
        }
    }

    /**
     * 读取源文件
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

            write(inputStream, outputStream, BUF_SIZE);
            byte[] bytes = outputStream.toByteArray();
            return bytes;
        } catch (IOException e) {
            logger.error("file = " + file, e);
            return null;
        } finally {
            closeQuietly(outputStream);
            closeQuietly(inputStream);
        }
    }

    /**
     * 将byte数组写入文件
     */
    public static boolean writeFile(byte[] value, File file) {
        if (null == value || null == file) {
            logger.error("value = {},file = {}", value, file);
            return false;
        }

        if (file.isDirectory()) {
            logger.error("file.isDirectory() = " + true);
            return false;
        }

        file.delete();
        boolean createFile = createFile(file);
        if (!createFile) {
            logger.error("createFile = false,file = " + file);
            return false;
        }

        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file));
            outputStream.write(value);
            outputStream.flush();
            return true;
        } catch (IOException e) {
            logger.error("file = " + file, e);
            return false;
        } finally {
            closeQuietly(outputStream);
        }
    }

    /**
     * 读取{@link java.util.zip.ZipFile}中{@link String}文件写入到{@link java.io.File}中
     *
     * @param zipfile   zip压缩包
     * @param entryName 需要读取的压缩包中文件文件名
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
                write(is, os, BUF_SIZE);
            } else {
                // log.i("JNI library " + soname.getAbsolutePath()
                // + " is up to date");
            }
            return true;
        } catch (Exception e) { // I am still lazy ~~~
            logger.error("zipfile = " + zipfile + ",entryName = " + entryName
                    + ",soFile = " + soFile, e);
            return false;
        } finally {
            closeQuietly(is);
            closeQuietly(os);
        }
    }

    /**
     * 将{@link java.io.InputStream}中内容写入{@link java.io.OutputStream}
     */
    public static void write(InputStream inputStream, OutputStream outputStream, int bufSize)
            throws IOException {
        int count = -1;
        byte[] buffer = new byte[bufSize];
        while ((count = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, count);
        }
    }

    public static void write(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        write(inputStream, outputStream, BUF_SIZE);
    }

    /**
     * 读取{@link java.io.InputStream}中内容，以byte[]返回
     */
    public static byte[] read(InputStream inputStream) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            write(inputStream, baos, BUF_SIZE);
            return baos.toByteArray();
        } catch (IOException e) {
            logger.error("", e);
            return null;
        } finally {
            closeQuietly(baos);
        }
    }

    /**
     * 对象序列化
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
            logger.error("", e);
            return null;
        } finally {
            IoUtil.closeQuietly(baos);
            IoUtil.closeQuietly(oos);
        }
    }

    /**
     * 返序列化
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
            logger.error("", e);
            return null;
        } finally {
            IoUtil.closeQuietly(baos);
            IoUtil.closeQuietly(ois);
        }
    }

    public static boolean checkDirectory(File file) {
        Assertions.checkState(file != null, "file = null");

        if (file.exists()) {
            if (!file.isDirectory()) {
                logger.error(file.getAbsolutePath() + " already exists and is not a directory");
                return false;
            }
        } else {
            if (!file.mkdirs()) {
                logger.error("Unable to create directory: " + file.getAbsolutePath());
                return false;
            }
        }
        return true;
    }

    private static int getShort(byte[] data) {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }

    public static byte[] decompressGZip(byte[] data) {
        byte[] h = new byte[2];
        h[0] = (data)[0];
        h[1] = (data)[1];
        int head = getShort(h);
        boolean t = head == 0x1f8b;
        InputStream in = null;
        ByteArrayOutputStream bos = null;

        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        try {
            if (t) {
                in = new GZIPInputStream(bis);
            } else {
                in = bis;
            }

            bos = new ByteArrayOutputStream();
            write(in, bos, BUF_SIZE);
            return bos.toByteArray();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            closeQuietly(in);
            closeQuietly(bos);
        }
    }
}