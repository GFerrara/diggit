package now.gf.diggit.java.reflection;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.Map;

import now.gf.diggit.java.reflection.model.ClassMetadata;
import now.gf.diggit.utils.LRUCache;

/**
 * <p>This is the entry-point class for getting class metadata info.
 * You can start either by creating a new simple instance of this class
 * or by providing an already-filled class metadata Map to its constructor.
 * Either way, everytime a call is issued for obtaining class metadata 
 * info, firstly a lookup is performed into an internal LRU cache,
 * then, if the required information aren't there, a new class metadata
 * object will be built for the required class and then cached for 
 * later use.</p>
 * 
 * @author Giorgio Ferrara
 *
 */
public class ClassIntrospector {
	private static final int DEFAULT_MAX_CACHE_SIZE = 100;
	private LRUCache<String, ClassMetadata> classMetadataCache;  // Holds class metadata info: key=<canonical class name>, value=<class metadata>

	/**
	 * Build a new <tt>ClassIntrospector</tt> with default-sized LRU cache.
	 */
	public ClassIntrospector() {
		classMetadataCache = new LRUCache<String, ClassMetadata>(DEFAULT_MAX_CACHE_SIZE);
	}
	
	/**
	 * Build a new <tt>ClassIntrospector</tt> by specifying maximum size of its internal LRU cache.
	 * 
	 * @param maxCacheSize maximum size of internal metadata cache
	 */
	public ClassIntrospector(final int maxCacheSize) {
		classMetadataCache = new LRUCache<String, ClassMetadata>(maxCacheSize);
	}
	
	/**
	 * Allows to register a listener associated to a specific annotation, i.e: everytime a specific annotation
	 * is reached the listener is called. 
	 * 
	 * @param annotation the class that corresponds to the annotation
	 * @param annotationListener the listener that will be called when the annotation is reached
	 */
	public <T extends Annotation> void registerAnnotationListener(final Class<T> annotation, final IAnnotationListener<T> annotationListener) {
		if (annotation == null || annotationListener == null) throw new IllegalArgumentException("Both annotation and annotation listener must be specified");

		AnnotationHelper.instance().registerAnnotationListener(annotation, annotationListener);
	}
	
	/**
	 * Populates internal metadata cache with class metadata information obtained
	 * analyzing each of the classes passed as a parameter. 
	 * 
	 * @param clzs classes to load metadata information from
	 * @throws ClassNotFoundException if one of the specified classes is not found in classpath
	 */
	public void loadClassesMetadata (final Class<?>... clzs) throws ClassNotFoundException {
		if (clzs == null || clzs.length == 0) return;
		
		for (Class<?> clz : clzs) {
			String clzName = clz.getCanonicalName();
			if (classMetadataCache.containsKey(clzName)) continue;
			
			ClassMetadata classMetadata = new ClassMetadata(clz);
			classMetadataCache.put(clzName, classMetadata);
		}
	}
	
	/**
	 * Merges internal cache info with data obtained from the passed map.
	 * 
	 * @param classMetadata a Map containing class metadata info
	 */
	public void addClassMetadataMapInfo (final Map<String, ClassMetadata> classMetadata) {
		if (classMetadata != null) classMetadataCache.putAll(classMetadata);
	}
	
	/**
	 * Returns metadata information for the requested class.
	 * 
	 * @param clz class to analyze
	 * @return metadata information for the specified class
	 * @throws ClassNotFoundException if the specified class is not found in classpath
	 */
	public ClassMetadata getClassMetadata(final Class<?> clz) throws ClassNotFoundException {
		if (clz == null) return null;
		
		// Check cache
		String clzName = clz.getCanonicalName();
		if (classMetadataCache.containsKey(clzName)) return classMetadataCache.get(clzName);
		
		// Build new metadata and put them into cache
		ClassMetadata classMetadata = new ClassMetadata(clz);
		classMetadataCache.put(clzName, classMetadata);
		return classMetadata;
	}
	
	/**
	 * Returns internal class metadata cache as unmodifiable map. 
	 * 
	 * @return read-only version of internal cache
	 * @see Collections
	 */
	public Map<String, ClassMetadata> getClassMetadataCache() {
		return Collections.unmodifiableMap(classMetadataCache);
	}
}