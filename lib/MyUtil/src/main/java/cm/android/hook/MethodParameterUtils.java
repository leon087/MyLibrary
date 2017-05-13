package cm.android.hook;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 */
public class MethodParameterUtils {

	public static Class<?>[] getAllInterface(Class clazz){
		HashSet<Class<?>> classes = new HashSet<>();
		getAllInterfaces(clazz,classes);
		Class<?>[] result=new Class[classes.size()];
		classes.toArray(result);
		return result;
	}


	public static void getAllInterfaces(Class clazz, HashSet<Class<?>> interfaceCollection) {
		Class<?>[] classes = clazz.getInterfaces();
		if (classes.length != 0) {
			interfaceCollection.addAll(Arrays.asList(classes));
		}
		if (clazz.getSuperclass() != Object.class) {
			getAllInterfaces(clazz.getSuperclass(), interfaceCollection);
		}
	}


}
