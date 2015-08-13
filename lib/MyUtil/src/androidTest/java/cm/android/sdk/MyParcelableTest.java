package cm.android.sdk;

import android.os.Bundle;
import android.test.InstrumentationTestCase;


public class MyParcelableTest extends InstrumentationTestCase {

    public void testGetData() throws Exception {
        MyParcelable<String> parcelable = MyParcelable.newParcelable();
        parcelable.setValue("value");

        Bundle bundle = new Bundle();
        bundle.putParcelable("123", parcelable);

        String rusult = MyParcelable.getData(bundle, "123");
        assertEquals(rusult, "value");
    }

    public void testGenerateBundle() throws Exception {
        MyParcelable<String> parcelable = MyParcelable.newParcelable();
        parcelable.setValue("value");

        Bundle bundle = MyParcelable.generateBundle("123", parcelable);

        MyParcelable rusult = MyParcelable.getData(bundle, "123");
        assertEquals("value", rusult.getValue());
    }

}
