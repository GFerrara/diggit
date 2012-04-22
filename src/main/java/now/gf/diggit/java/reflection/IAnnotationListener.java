package now.gf.diggit.java.reflection;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * <p>There is no standard, unique, way to extract info from a Java annotation,
 * therefore, for diving into annotations, we have to introduce classes that act
 * as <i>annotation listeners</i> so that, everytime an annotation is reached, 
 * the associated listener is called. This listener, in turn, provides the caller 
 * with a map of attributes (if any) that are bound to that specific annotation.</p>  
 * 
 * @author Giorgio Ferrara
 * @see Annotation
 *
 */
public interface IAnnotationListener <T extends Annotation> {
	/**
	 * Returns a map of annotation attributes or a null value if annotation doesn't expose attributes. 
	 * 
	 * @param annotation a Java annotation
	 * @return a map of annotation attributes (key=<annotation attribute name>, value=<annotation attribute value>)
	 */
	public Map<String, Object> getAttributesMap(T annotation);
}
