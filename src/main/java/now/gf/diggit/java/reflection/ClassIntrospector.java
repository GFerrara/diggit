package now.gf.diggit.java.reflection;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import now.gf.diggit.java.reflection.model.ClassMetadata;

/**
 * 
 * @author Giorgio Ferrara
 *
 */
public class ClassIntrospector {
	private Map<String, ClassMetadata> metadataCacheMap;

	public ClassIntrospector() {
		this.metadataCacheMap = new HashMap<String, ClassMetadata>();
	}
	
	public ClassIntrospector(final Map<String, ClassMetadata> cacheMap) {
		this.metadataCacheMap = cacheMap == null ? new HashMap<String, ClassMetadata>() : cacheMap;
	}
	
	public <T extends Annotation> void registerAnnotationListener(final Class<T> annotation, IAnnotationListener<T> annotationListener) {
		if (annotation == null || annotationListener == null) throw new IllegalArgumentException("Both annotation and annotation listener must be specified");
			
		AnnotationHelper.instance().registerAnnotationListener(annotation, annotationListener);
	}
	
	public void loadClassesMetadata (final Class<?>... clzs) throws ClassNotFoundException {
		if (clzs == null || clzs.length == 0) return;
		
		for (Class<?> clz : clzs) {
			String clzName = clz.getCanonicalName();
			if (metadataCacheMap.containsKey(clzName)) continue;
			
			ClassMetadata classMetadata = new ClassMetadata(clz);
			metadataCacheMap.put(clzName, classMetadata);
		}
	}
	
	public ClassMetadata getClassMetadata(final Class<?> clz) throws ClassNotFoundException {
		if (clz == null) return null;
		
		String clzName = clz.getCanonicalName();
		if (metadataCacheMap.containsKey(clzName)) return metadataCacheMap.get(clzName);
		
		ClassMetadata classMetadata = new ClassMetadata(clz);
		metadataCacheMap.put(clzName, classMetadata);
		return classMetadata;
	}
	
	public Map<String, ClassMetadata> getMetadataCacheMap() {
		return metadataCacheMap;
	}
}