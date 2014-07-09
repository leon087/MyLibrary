package cm.android.util;

import cm.android.global.MyParcelable;

import java.lang.reflect.Array;
import java.util.*;

/**
 * 泛型对象创建器
 */
public class ObjectUtil {
	private ObjectUtil() {
	}

	/**
	 * 创建一个{@code HashMap}对象
	 * 
	 * @return
	 */
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<K, V>();
	}

	/**
	 * 创建一个{@code LinkedList}对象
	 * 
	 * @return
	 */
	public static <E> LinkedList<E> newLinkedList() {
		return new LinkedList<E>();
	}

	/**
	 * 创建一个{@code HashSet}对象
	 * 
	 * @return
	 */
	public static <E> HashSet<E> newHashSet() {
		return new HashSet<E>();
	}

	/**
	 * Creates an empty {@code ArrayList} instance.
	 * 
	 * <p>
	 * <b>Note:</b> if you only need an <i>immutable</i> empty List, use
	 * {@link Collections#emptyList} instead.
	 * 
	 * @return a newly-created, initially-empty {@code ArrayList}
	 */
	public static <E> ArrayList<E> newArrayList() {
		return new ArrayList<E>();
	}

	/**
	 * Creates a resizable {@code ArrayList} instance containing the given
	 * elements.
	 * 
	 * <p>
	 * <b>Note:</b> due to a bug in javac 1.5.0_06, we cannot support the
	 * following:
	 * 
	 * <p>
	 * {@code List<Base> list = Lists.newArrayList(sub1, sub2);}
	 * 
	 * <p>
	 * where {@code sub1} and {@code sub2} are references to subtypes of
	 * {@code Base}, not of {@code Base} itself. To get around this, you must
	 * use:
	 * 
	 * <p>
	 * {@code List<Base> list = Lists.<Base>newArrayList(sub1, sub2);}
	 * 
	 * @param elements
	 *            the elements that the list should contain, in order
	 * @return a newly-created {@code ArrayList} containing those elements
	 */
	public static <E> ArrayList<E> newArrayList(E... elements) {
		int capacity = (elements.length * 110) / 100 + 5;
		ArrayList<E> list = new ArrayList<E>(capacity);
		Collections.addAll(list, elements);
		return list;
	}

	/**
	 * 创建一个{@code MyParcelable}对象
	 * 
	 * @return
	 */
	public static <E> MyParcelable<E> newParcelable() {
		return new MyParcelable<E>();
	}

	public static <T> T[] toArray(Class<?> cls, ArrayList<T> items) {
		if (items == null || items.size() == 0) {
			return (T[]) Array.newInstance(cls, 0);
		}
		return items.toArray((T[]) Array.newInstance(cls, items.size()));
	}
}
