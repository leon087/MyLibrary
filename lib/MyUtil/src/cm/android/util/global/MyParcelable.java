package cm.android.util.global;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 封装的Parcelable对象，可保存Object
 * 
 * @param <T>
 *            保存的Object对象数据类型
 */
public class MyParcelable<T> implements Parcelable {
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

	/**
	 * 获取value
	 * 
	 * @return
	 */
	public T getValue() {
		return (T) value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}
