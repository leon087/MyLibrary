package cm.android.apn;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import android.content.Context;

import cm.android.util.BuildConfig;

import static junit.framework.Assert.assertEquals;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 22)
public class ApnUtilTest {

//    @Test
//    public void testAddAPN() throws Exception {//在有sim卡的手机上测试
//        Context context = RuntimeEnvironment.application;
//        ApnModel testModel = new ApnModel();
//        int result = ApnUtil.addAPN(context, testModel);
//        assertEquals(result, -1);
//    }

    @Test
    public void testSetApn() throws Exception {
        Context context = RuntimeEnvironment.application;
        boolean temp = ApnUtil.setApn(context, 0);
        assertEquals(temp, false);
    }

    @Test
    public void testQueryByApn() throws Exception {
        Context context = RuntimeEnvironment.application;
        int temp = ApnUtil.queryByApn(context, "ajsd");
        assertEquals(temp, -1);
    }
}
