package now.gf.diggit.java.reflection;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * 
 * @author Giorgio Ferrara
 *
 */
public interface IAnnotationListener <T extends Annotation> {
	public Map<String, Object> getAttributesMap(T annotation);
}
