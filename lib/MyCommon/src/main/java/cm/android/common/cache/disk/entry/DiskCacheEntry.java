package cm.android.common.cache.disk.entry;

import cm.android.util.Charsets;
import cm.android.util.IoUtil;
import cm.android.util.StrictLineReader;
import com.jakewharton.disklrucache.DiskLruCache;

import java.io.*;

public abstract class DiskCacheEntry {
    private int entry = 0;

    public DiskCacheEntry(int entry) {
        this.entry = entry;
    }

    /**
     * 从DiskLruCache.Snapshot中读取数据
     *
     * @param snapshot
     * @throws IOException
     */
    public final void readFrom(DiskLruCache.Snapshot snapshot)
            throws IOException {
        InputStream in = snapshot.getInputStream(entry);
        StrictLineReader reader = null;
        try {
            reader = new StrictLineReader(in, Charsets.UTF_8);
            read(reader);
        } finally {
            IoUtil.closeQuietly(reader);
            IoUtil.closeQuietly(in);
        }
    }

    /**
     * 向DiskLruCache.Editor中写入数据
     *
     * @param editor
     * @throws IOException
     */
    public final void writeTo(DiskLruCache.Editor editor) throws IOException {
        OutputStream out = editor.newOutputStream(entry);
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(out,
                    Charsets.UTF_8));
            write(writer);
        } finally {
            IoUtil.closeQuietly(writer);
            IoUtil.closeQuietly(out);
        }
    }

    protected abstract void read(StrictLineReader reader) throws IOException;

    protected abstract void write(Writer writer) throws IOException;

}
