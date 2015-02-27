package cm.android.common.cache.disk.entry;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import cm.java.util.ObjectUtil;
import cm.java.util.StrictLineReader;
import cm.java.util.Utils;

public class HttpCacheEntry extends DiskCacheEntry {

    private String uri;

    private String time;

    private Map<String, String> headers;

    private String content;

    public HttpCacheEntry() {
        super(0);
    }

    private Map<String, String> readHeader(StrictLineReader reader)
            throws IOException {
        // 读head条数
        int length = Integer.parseInt(reader.readLine());
        if (length == -1 || length == 0) {
            return null;
        }

        Map<String, String> headMap = ObjectUtil.newHashMap();
        for (int i = 0; i < length; i++) {
            String line = reader.readLine();
            String[] head = line.split(":");
            headMap.put(head[0], head[1]);
        }
        return headMap;
    }

    @Override
    protected void read(StrictLineReader reader) throws IOException {
        uri = reader.readLine();
        time = reader.readLine();
        content = reader.readLine();
        headers = readHeader(reader);
    }

    @Override
    protected void write(Writer writer) throws IOException {
        // 写uri
        writer.write(uri + '\n');
        // 写入时间
        writer.write(String.valueOf(System.currentTimeMillis()) + '\n');
        // 写body
        writer.write(Utils.replaceBlank(content) + '\n');

        // 写head条数
        if (!Utils.isEmpty(headers)) {
            writer.write(String.valueOf(headers.size()) + '\n');
            // 写header
            for (String name : headers.keySet()) {
                writer.write(name + ":" + headers.get(name) + '\n');
            }
        } else {
            writer.write("0" + '\n');
        }
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    // public void setTime(String time) {
    // this.time = time;
    // }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }
}
