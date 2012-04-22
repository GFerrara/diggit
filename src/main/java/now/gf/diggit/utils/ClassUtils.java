package now.gf.diggit.utils;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 
 * @author Giorgio Ferrara
 *
 */
public class ClassUtils {
	private ClassUtils() {}

	public static Class<?> getActualType(final Field field) {
		Type type = field.getGenericType();
		if (type instanceof ParameterizedType) {
			return (Class<?>)((ParameterizedType) type).getActualTypeArguments()[0];
	    }
		if (field.getType().isArray()) {
			return field.getType().getComponentType();
		}
		// Field type is a supported collection type but doesn't specify a contained objects
		return null;
	}
	
	public static boolean isImplementingInterface(final Class<?> clz, final Class<?> clzInterface) {
		for (Class<?> interf : clz.getInterfaces()) {
			if (interf == clzInterface) {
				return true;
			}
		}
		return false;
	}
}