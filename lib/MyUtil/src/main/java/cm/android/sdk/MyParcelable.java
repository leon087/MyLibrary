package cm.android.sdk;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 封装的Parcelable对象，可保存Object
 *
 * @param <T> 保存的Object对象数据类型
 */
public class MyParcelable<T> implements Parcelable {

    public static final Parcelable.Creator<MyParcelable> CREATOR = new Creator<MyParcelable>() {
        @Override
        public MyParcelable[] newArray(int size) {
            return new MyParcelable[size];
        }

        @Override
        public MyParcelable<Object> createFromParcel(Parcel source) {
            Object value = source.readValue(Object.class.getClassLoader());
            MyParcelable<Object> myParcelable = new MyParcelable<Object>();
            myParcelable.setValue(value);
            return myParcelable;
        }
    };

    private Object value;

    public MyParcelable() {
    }

    public MyParcelable(Parcel source) {
        value = source.readValue(Object.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(value);
    }

    /**
     * 获取value
     */
    public T getValue() {
        return (T) value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    /**
     * 创建一个{@code MyParcelable}对象
     */
    public static <E> MyParcelable<E> newParcelable() {
        return new MyParcelable<E>();
    }

    public static <E> MyParcelable<E> newParcelable(E value) {
        MyParcelable myParcelable = new MyParcelable<E>();
        myParcelable.setValue(value);
        return myParcelable;
    }

    /**
     * 获取Bundle中数据
     */
    public static <T> T getData(Bundle bundle, String key) {
        if (bundle == null) {
            return null;
        }
        MyParcelable<T> parcelable = bundle.getParcelable(key);
        if (parcelable != null) {
            return parcelable.getValue();
        }
        return null;
    }

    /**
     * 将object包装至Bundle中
     */
    public static Bundle generateBundle(String key, Object object) {
        Bundle bundle = new Bundle();
        MyParcelable<Object> parcelable = MyParcelable.newParcelable();
        parcelable.setValue(object);
        bundle.putParcelable(key, parcelable);
        return bundle;
    }
}
