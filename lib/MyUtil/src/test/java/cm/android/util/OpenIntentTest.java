package cm.android.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import android.content.Context;
import android.content.Intent;

import java.io.File;

import static junit.framework.TestCase.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class OpenIntentTest {

    @Test
    public void testGetHtmlFileIntent() throws Exception {
        Context context = TestUtil.getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getHtmlFileIntent(file);
        boolean result = intent.getType().equals("text/html");
        assertEquals(true, result);
    }

    @Test
    public void testGetImageFileIntent() throws Exception {
        Context context = TestUtil.getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getImageFileIntent(file);
        boolean result = intent.getType().equals("image/*");
        assertEquals(true, result);
    }

    @Test
    public void testGetPdfFileIntent() throws Exception {
        Context context = TestUtil.getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getPdfFileIntent(file);
        boolean result = intent.getType().equals("application/pdf");
        assertEquals(true, result);
    }

    @Test
    public void testGetTextFileIntent() throws Exception {
        Context context = TestUtil.getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getTextFileIntent(file);
        boolean result = intent.getType().equals("text/plain");
        assertEquals(true, result);
    }

    @Test
    public void testGetAudioFileIntent() throws Exception {
        Context context = TestUtil.getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getAudioFileIntent(file);
        boolean result = intent.getType().equals("audio/*");
        assertEquals(true, result);
    }

    @Test
    public void testGetVideoFileIntent() throws Exception {
        Context context = TestUtil.getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getVideoFileIntent(file);
        boolean result = intent.getType().equals("video/*");
        assertEquals(true, result);
    }

    @Test
    public void testGetChmFileIntent() throws Exception {
        Context context = TestUtil.getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getChmFileIntent(file);
        boolean result = intent.getType().equals("application/x-chm");
        assertEquals(true, result);
    }

    @Test
    public void testGetWordFileIntent() throws Exception {
        Context context = TestUtil.getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getWordFileIntent(file);
        boolean result = intent.getType().equals("application/msword");
        assertEquals(true, result);
    }

    @Test
    public void testGetExcelFileIntent() throws Exception {
        Context context = TestUtil.getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getExcelFileIntent(file);
        boolean result = intent.getType().equals("application/vnd.ms-excel");
        assertEquals(true, result);
    }

    @Test
    public void testGetPPTFileIntent() throws Exception {
        Context context = TestUtil.getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getPPTFileIntent(file);
        boolean result = intent.getType().equals("application/vnd.ms-powerpoint");
        assertEquals(true, result);
    }

    @Test
    public void testGetApkFileIntent() throws Exception {
        Context context = TestUtil.getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getApkFileIntent(file);
        boolean result = intent.getType().equals("application/vnd.android.package-archive");
        assertEquals(true, result);
    }
}
