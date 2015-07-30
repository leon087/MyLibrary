package cm.android.common.cache.core;


import android.test.InstrumentationTestCase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cm.android.common.am.ui.ApplicationsState;
import cm.java.util.HexUtil;

public class CacheLoaderTest extends InstrumentationTestCase {

    public void testToKey() throws Exception {
        try {
            String uri = "dsiudsfaa";
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] md5bytes = messageDigest.digest(uri.getBytes());
            String result = HexUtil.encode(md5bytes);
            if (result.equals("")) {
                assertEquals(true, false);
            } else {
                assertEquals(true, true);
            }
        } catch (NoSuchAlgorithmException e) {
            assertEquals(true, false);
        }
    }

}