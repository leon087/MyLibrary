package cm.android.sdk;

import android.os.Bundle;
import android.test.InstrumentationTestCase;


public class MyParcelableTest extends InstrumentationTestCase {

    public void testGetData() throws Exception {
        MyParcelable<String> parcelable = MyParcelable.newParcelable();
        parcelable.setValue("value");
        Bundle bundle = new Bundle();
        bundle.putParcelable("123", parcelable);
        String rusult = "";
        rusult = MyParcelable.getData(bundle, "123");
        assertEquals(rusult, "value");
    }

    public void testGenerateBundle() throws Exception {
        MyParcelable<String> parcelable = MyParcelable.newParcelable();
        parcelable.setValue("value");
        Bundle bundle = new Bundle();
        Bundle bundle1 = new Bundle();
        bundle.putParcelable("123", parcelable);
        bundle1 = MyParcelable.generateBundle("123", "value");
        String rusult1 = MyParcelable.getData(bundle, "123");
        String rusult2 = MyParcelable.getData(bundle1, "123");
        assertEquals(rusult1, rusult2);
    }

}
