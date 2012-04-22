package now.gf.diggit.java.reflection.model;


import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import now.gf.diggit.java.reflection.AnnotationHelper;
import now.gf.diggit.utils.ClassUtils;

/**
 * 
 * @author Giorgio Ferrara
 *
 */
public class FieldMetadata implements Serializable {
	private static final long serialVersionUID = -8634203172867339309L;

	private String qualifiedClassName;
	private String name;
	private String typeName;
	private String baseTypeName;
	private String getterMethodName;
	private String setterMethodName;
	private int modifiers;
	private boolean isArray;
	private boolean isList;
	private boolean isSet;
	private boolean isMultiple;
	private Set<AnnotationMetadata> annotations;
	
	FieldMetadata(final ClassMetadata classMetadata, final Field field) {
		if (field == null) throw new IllegalArgumentException("Unspecified field");

		Class<?> type = (Class<?>) field.getType();
		
		this.qualifiedClassName = classMetadata.getQualifiedName();
		this.name = field.getName();
		this.typeName = type.getCanonicalName();
		this.modifiers = field.getModifiers(); 
		this.isArray = type.isArray();
		this.isList = Collection.class.isAssignableFrom(type);
		this.isSet = Set.class.isAssignableFrom(type);
		this.isMultiple = (isArray || isList || isSet);
		if (isMultiple) {
			Class<?> baseType = ClassUtils.getActualType(field);
			this.baseTypeName = baseType.getCanonicalName();
		}
		
		this.annotations = new HashSet<AnnotationMetadata>();
		if (AnnotationHelper.instance().isAnnotationAware()) {
			for (Annotation annotation : field.getAnnotations()) {
				if (AnnotationHelper.instance().isAnnotationManaged(annotation)) {
					AnnotationMetadata annotationMetadata = new AnnotationMetadata(this, annotation.annotationType());
					annotationMetadata.setAttributesMap(AnnotationHelper.instance().getAttributesMap(annotation));
					this.annotations.add(annotationMetadata);
				}
			}
		}
	}
	
	public String getQualifiedClassName() {
		return qualifiedClassName;
	}
	
	public String getName() {
		return name;
	}
	
	public String getTypeName() {
		return typeName;
	}
	
	public String getBaseTypeName() {
		return baseTypeName;
	}
	
	public String getGetterMethodName() {
		return getterMethodName;
	}
	
	public String getSetterMethodName() {
		return setterMethodName;
	}
	
	public int getModifiers() {
		return modifiers;
	}
	
	public boolean isArray() {
		return isArray;
	}
	
	public boolean isList() {
		return isList;
	}
	
	public boolean isSet() {
		return isSet;
	}
	
	public boolean isMultiple() {
		return isMultiple;
	}
	
	public Set<AnnotationMetadata> getAnnotations() {
		return annotations;
	}
	
	public AnnotationMetadata getNamedAnnotation(final Class<? extends Annotation> clzAnnotation) {
		if (clzAnnotation == null) return null;
		
		String annotationQualifiedName = clzAnnotation.getCanonicalName();
		for (AnnotationMetadata annotation : annotations) {
			if (annotation.getQualifiedName().equals(annotationQualifiedName)) return annotation;
		}
		return null;
	}
	
	void setGetterMethodName(final String getterMethodName) {
		this.getterMethodName = getterMethodName;
	}
	
	void setSetterMethodName(final String setterMethodName) {
		this.setterMethodName = setterMethodName;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder("FieldMetadata [")
			.append('\n').append("qualifiedClassName=").append(qualifiedClassName)
			.append('\n').append("name=").append(name)
			.append('\n').append("typeName=").append(typeName)
			.append('\n').append("baseTypeName=").append(baseTypeName)
			.append('\n').append("getterMethodName=").append(getterMethodName)
			.append('\n').append("setterMethodName=").append(setterMethodName)
			.append('\n').append("modifiers=").append(modifiers)
			.append('\n').append("isArray=").append(isArray)
			.append('\n').append("isList=").append(isList)
			.append('\n').append("isSet=").append(isSet)
			.append('\n').append("isMultiple=").append(isMultiple)
			.append('\n').append("annotations=[");
		for (AnnotationMetadata annotationMetadata : annotations) {
			buffer.append('\n').append(annotationMetadata.toString());
		}
		buffer.append('\n').append(']');
		return buffer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((qualifiedClassName == null) ? 0 : qualifiedClassName
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FieldMetadata other = (FieldMetadata) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (qualifiedClassName == null) {
			if (other.qualifiedClassName != null)
				return false;
		} else if (!qualifiedClassName.equals(other.qualifiedClassName))
			return false;
		return true;
	}
}