package cm.android.util;

import android.content.Context;
import android.content.Intent;
import android.test.InstrumentationTestCase;

import java.io.File;

public class OpenIntentTest extends InstrumentationTestCase {

    public void testGetHtmlFileIntent() throws Exception {
        Context context = getInstrumentation().getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getHtmlFileIntent(file);
        boolean result = intent.getType().equals("text/html");
        assertEquals(true, result);
    }

    public void testGetImageFileIntent() throws Exception {
        Context context = getInstrumentation().getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getImageFileIntent(file);
        boolean result = intent.getType().equals("image/*");
        assertEquals(true, result);
    }

    public void testGetPdfFileIntent() throws Exception {
        Context context = getInstrumentation().getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getPdfFileIntent(file);
        boolean result = intent.getType().equals("application/pdf");
        assertEquals(true, result);
    }

    public void testGetTextFileIntent() throws Exception {
        Context context = getInstrumentation().getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getTextFileIntent(file);
        boolean result = intent.getType().equals("text/plain");
        assertEquals(true, result);
    }

    public void testGetAudioFileIntent() throws Exception {
        Context context = getInstrumentation().getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getAudioFileIntent(file);
        boolean result = intent.getType().equals("audio/*");
        assertEquals(true, result);
    }

    public void testGetVideoFileIntent() throws Exception {
        Context context = getInstrumentation().getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getVideoFileIntent(file);
        boolean result = intent.getType().equals("video/*");
        assertEquals(true, result);
    }

    public void testGetChmFileIntent() throws Exception {
        Context context = getInstrumentation().getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getChmFileIntent(file);
        boolean result = intent.getType().equals("application/x-chm");
        assertEquals(true, result);
    }

    public void testGetWordFileIntent() throws Exception {
        Context context = getInstrumentation().getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getWordFileIntent(file);
        boolean result = intent.getType().equals("application/msword");
        assertEquals(true, result);
    }

    public void testGetExcelFileIntent() throws Exception {
        Context context = getInstrumentation().getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getExcelFileIntent(file);
        boolean result = intent.getType().equals("application/vnd.ms-excel");
        assertEquals(true, result);
    }

    public void testGetPPTFileIntent() throws Exception {
        Context context = getInstrumentation().getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getPPTFileIntent(file);
        boolean result = intent.getType().equals("application/vnd.ms-powerpoint");
        assertEquals(true, result);
    }

    public void testGetApkFileIntent() throws Exception {
        Context context = getInstrumentation().getContext();
        File file = EnvironmentUtil.getExternalDir(context);
        Intent intent = OpenIntent.getApkFileIntent(file);
        boolean result = intent.getType().equals("application/vnd.android.package-archive");
        assertEquals(true, result);
    }
}
