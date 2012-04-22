package now.gf.diggit.java.reflection;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author Giorgio Ferrara
 *
 */
@SuppressWarnings("unchecked")
public class AnnotationHelper {
	private static final AnnotationHelper instance = new AnnotationHelper();
	private Map<Class, IAnnotationListener> annotationListenersMap;
	
	private AnnotationHelper() {
		this.annotationListenersMap = new HashMap();
	}
	
	public static AnnotationHelper instance() {
		return instance;
	}
	
	<T extends Annotation> void registerAnnotationListener(final Class<T> annotation, final IAnnotationListener<T> annotationListener) {
		annotationListenersMap.put(annotation, annotationListener);
	}
	
	public boolean isAnnotationAware() {
		return !annotationListenersMap.isEmpty();
	}
	
	public boolean isAnnotationManaged(final Annotation annotation) {
		return annotationListenersMap.containsKey(annotation.annotationType());
	}
	
	public Map<String, Object> getAttributesMap(final Annotation annotation) {
		if (!annotationListenersMap.containsKey(annotation.annotationType())) return null;
		
		IAnnotationListener annotationListener = annotationListenersMap.get(annotation.annotationType());
		return annotationListener.getAttributesMap(annotation);
	}
}
