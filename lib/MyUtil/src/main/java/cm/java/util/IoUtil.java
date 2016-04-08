package cm.java.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cm.java.cmd.CmdExecute;

/**
 * IO读写Util类
 */
public class IoUtil {

    private static final int BUF_SIZE = 8 * 1024;

    private static final Logger logger = LoggerFactory.getLogger("util");

    private IoUtil() {
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
            logger.error(e.getMessage(), e);
        }
    }

    public static boolean createFile(File file) {
        if (file.isDirectory()) {
            logger.error("file.isDirectory() = " + true);
            return false;
        }

        if (file.exists()) {
            return true;
        }

        File parent = file.getParentFile();
        if (checkDirectory(parent)) {
            try {
                return file.createNewFile();
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

        File[] fileList = dir.listFiles();
        if (fileList == null || fileList.length <= 0) {
            return true;
        }

        boolean result = true;
        for (File tmpFile : fileList) {
            result &= delete(tmpFile);
        }
        return result;
    }

    /**
     * API方式删除文件
     */
    public static boolean delete(File file) {
        if (file == null) {
            return true;
        }

        if (file.isFile()) {
            return deleteInternal(file);
        }

        File[] fileList = file.listFiles();
        if (fileList == null || fileList.length <= 0) {
            return deleteInternal(file);
        }

        boolean result = true;
        for (File tmpFile : fileList) {
            result &= delete(tmpFile);
        }
        //删除最上层目录
        result &= deleteInternal(file);

        return result;
    }

    /**
     * 通过rm命令删除文件
     */
    public static boolean deleteFile(File file) {
        if (file == null) {
            logger.error("file = null");
            return true;
        }

        String[] cmd = new String[]{
                "rm", "-fr", "--", file.getAbsolutePath()
        };
        CmdExecute.exec(cmd);

        boolean exist = file.exists();
        logger.info("file.exists() = ", exist);
        if (!exist) {
            return true;
        }

        return delete(file);
    }

    /**
     * 删除文件，如果该文件是个目录，则会删除该目录以及该目录下所有文件
     */
    private static boolean deleteInternal(File file) {
        try {
            return file.delete();
        } catch (SecurityException se) {
            logger.error("dir = {},se = {}", file.getAbsolutePath(), se.getMessage());
            return false;
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

    public static String[] getFiles(File fir) {
        if (!fir.isDirectory()) {
            return new String[0];
        }
        return fir.list();
    }

    public static List<File> getDirs(File dir) {
        List<File> dirs = ObjectUtil.newArrayList();
        if (!dir.isDirectory()) {
            return dirs;
        }
        dirs.add(dir);

        File[] files = dir.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory() && !pathname.getName().equals(".")
                        && !pathname.getName().equals("..");
            }
        });

        if (files == null || files.length == 0) {
            return dirs;
        }

        for (File tmpDir : files) {
            List<File> tmpDirList = getDirs(tmpDir);
            dirs.addAll(tmpDirList);
        }

        return dirs;
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
     * 获取指定目录下文件大小，不包含目录
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

        boolean createFile = createFile(file);
        if (!createFile) {
            logger.error("createFile = false,file = " + file);
            return false;
        }

        OutputStream outputStream = null;
        try {
            outputStream = new BufferedOutputStream(new FileOutputStream(file, false));
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
    public static boolean writeZipFile(ZipFile zipfile, String entryName, File soFile) {
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
        } catch (IOException e) {
            logger.error("zipfile = {},entryName = {},soFile = {},e = {}", zipfile, entryName, soFile, e);
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
            logger.error(e.getMessage(), e);
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
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
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
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return null;
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
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

    public static File buildPath(File base, String... segments) {
        File cur = base;
        for (String segment : segments) {
            if (cur == null) {
                cur = new File(segment);
            } else if (segment != null) {
                cur = new File(cur, segment);
            }
        }
        return cur;
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

    public static boolean compress(File srcFile, File destFile) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(new FileInputStream(srcFile));
            os = new BufferedOutputStream(new FileOutputStream(destFile));

            return compress(is, os);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            IoUtil.closeQuietly(is);
            IoUtil.closeQuietly(os);
        }

    }

    public static boolean decompress(File srcFile, File destFile) {
        InputStream is = null;
        OutputStream os = null;
        try {
            is = new BufferedInputStream(new FileInputStream(srcFile));
            os = new BufferedOutputStream(new FileOutputStream(destFile));

            return decompress(is, os);
        } catch (FileNotFoundException e) {
            logger.error(e.getMessage(), e);
            return false;
        } finally {
            IoUtil.closeQuietly(is);
            IoUtil.closeQuietly(os);
        }
    }

    public static boolean compress(InputStream is, OutputStream os) {
        try {
            GZIPOutputStream gos = new GZIPOutputStream(os);
            write(is, gos);
            gos.finish();
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public static boolean decompress(InputStream is, OutputStream os) {
        try {
            GZIPInputStream gis = new GZIPInputStream(is);
            write(gis, os);
            return true;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    public static final Properties loadProperties(File file) {
        Properties properties = new Properties();
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(file));
            properties.load(in);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeQuietly(in);
        }
        return properties;
    }
}