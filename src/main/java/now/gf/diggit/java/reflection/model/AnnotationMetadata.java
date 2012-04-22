package now.gf.diggit.java.reflection.model;


import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Giorgio Ferrara
 *
 */
public class AnnotationMetadata extends AbstractQualifiedMetadata {
	private static final long serialVersionUID = 3093649315366349575L;

	private ClassMetadata classMetadata;
	private FieldMetadata fieldMetadata;
	private Map<String, Object> attributesMap;
	
	AnnotationMetadata(final ClassMetadata classMetadata, final Class<? extends Annotation> annotation) {
		super(annotation);
		this.classMetadata = classMetadata;
		this.attributesMap = new HashMap<String, Object>();
	}
	
	AnnotationMetadata(final FieldMetadata fieldMetadata, final Class<? extends Annotation> annotation) {
		super(annotation);
		this.fieldMetadata = fieldMetadata;
		this.attributesMap = new HashMap<String, Object>();
	}
	
	
	public Object getParentMetadata() {
		return classMetadata == null ? fieldMetadata : classMetadata;
	}
	
	public Map<String, Object> getAttributesMap() {
		return attributesMap;
	}

	public Object getAttribute(final String name) {
		if (StringUtils.isBlank(name)) throw new IllegalArgumentException("Unspecified attribute name");
		return attributesMap.get(name);
	}
	
	public void addAttribute(final String name, final Object value) {
		if (StringUtils.isBlank(name)) throw new IllegalArgumentException("Unspecified attribute name");
		attributesMap.put(name, value);
	}
	
	public void setAttributesMap(final Map<String, Object> attributesMap) {
		if (attributesMap == null) this.attributesMap.clear(); else this.attributesMap = attributesMap;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder("AnnotationMetadata [");
		buffer.append('\n').append("qualifiedName=").append(qualifiedName)
			.append('\n').append("package=").append(packageName)
			.append('\n').append("name=").append(name);
		if (!attributesMap.isEmpty()) {
			for (String key : attributesMap.keySet()) {
				buffer.append('\n').append(key).append("=").append(attributesMap.get(key).toString());
			}
		}
		buffer.append('\n').append(']');
		return buffer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ ((classMetadata == null) ? 0 : classMetadata.hashCode());
		result = prime * result
				+ ((fieldMetadata == null) ? 0 : fieldMetadata.hashCode());
		result = prime * result
				+ ((qualifiedName == null) ? 0 : qualifiedName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnnotationMetadata other = (AnnotationMetadata) obj;
		if (classMetadata == null) {
			if (other.classMetadata != null)
				return false;
		} else if (!classMetadata.equals(other.classMetadata))
			return false;
		if (fieldMetadata == null) {
			if (other.fieldMetadata != null)
				return false;
		} else if (!fieldMetadata.equals(other.fieldMetadata))
			return false;
		if (qualifiedName == null) {
			if (other.qualifiedName != null)
				return false;
		} else if (!qualifiedName.equals(other.qualifiedName))
			return false;
		return true;
	}
}