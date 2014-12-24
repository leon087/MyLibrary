package cm.android.common.http.volley;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class VolleyUtil {

    private static final Logger logger = LoggerFactory.getLogger("volley");

    public static String parseBody(Map<String, String> body) {
        if (body == null) {
            return null;
        }

        return new JSONObject(body).toString();
    }

    private static int getShort(byte[] data) {
        return (int) ((data[0] << 8) | data[1] & 0xFF);
    }

    public static String getStringFromZip(byte[] data) {
        byte[] h = new byte[2];
        h[0] = (data)[0];
        h[1] = (data)[1];
        int head = getShort(h);
        boolean t = head == 0x1f8b;
        InputStream in = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(data);
            if (t) {
                in = new GZIPInputStream(bis);
            } else {
                in = bis;
            }
            br = new BufferedReader(new InputStreamReader(in),
                    1000);
            for (String line = br.readLine(); line != null; line = br.readLine()) {
                sb.append(line);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        } finally {
            closeQuietly(in);
            closeQuietly(br);
        }
        return sb.toString();
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
            int count;
            byte[] tmp = new byte[20148];
            while ((count = in.read(tmp, 0, 2048)) != -1) {
                bos.write(data, 0, count);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        } finally {
            closeQuietly(in);
            closeQuietly(bos);
        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception e) {
                logger.error("", e);
            }
        }
    }
}
