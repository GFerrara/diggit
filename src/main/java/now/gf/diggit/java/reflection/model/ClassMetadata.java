package now.gf.diggit.java.reflection.model;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import now.gf.diggit.java.reflection.AnnotationHelper;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.Field;
import org.apache.bcel.classfile.JavaClass;
import org.apache.commons.lang.StringUtils;

/**
 * 
 * @author Giorgio Ferrara
 *
 */
public class ClassMetadata extends AbstractQualifiedMetadata {
	private static final long serialVersionUID = 4846394635597769806L;
	
	private Set<FieldMetadata> fields;  // Non-null set of fields
	private Set<AnnotationMetadata> annotations;   // Non-null set of annotations
	
	public ClassMetadata(final Class<?> clz) throws ClassNotFoundException {
		super(clz);
		
		// In order to get fields in the exact order they are declared, we use BCEL library for class file reading
		// (Java introspection can't ensure this)
		JavaClass javaClass = Repository.lookupClass(qualifiedName);
		if (javaClass == null) throw new ClassNotFoundException(qualifiedName);

		// Fields
		this.fields = new LinkedHashSet<FieldMetadata>();
		// Get fields in the order they are declared
		Field[] clzFields = javaClass.getFields();
		// Get info on getter and setter methods
		Map<String, Method> getterMap = new HashMap<String, Method>(), setterMap = new HashMap<String, Method>();
		BeanInfo beanInfo;
		try {
			beanInfo = Introspector.getBeanInfo(clz);
			if (beanInfo != null) {
				PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
				for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
					getterMap.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod());
					setterMap.put(propertyDescriptor.getName(), propertyDescriptor.getWriteMethod());
				}
			}
		} catch (IntrospectionException e) {}
		// Save info into metadata
		try {
			for (Field field : clzFields) {
				String name = field.getName();
				FieldMetadata fieldMetadata = new FieldMetadata(this, clz.getDeclaredField(name));
				if (getterMap.get(name) != null) fieldMetadata.setGetterMethodName(getterMap.get(name).getName());
				if (setterMap.get(name) != null) fieldMetadata.setSetterMethodName(setterMap.get(name).getName());
				fields.add(fieldMetadata);
			}
		} catch (NoSuchFieldException e) {}
		
		// Annotations
		this.annotations = new HashSet<AnnotationMetadata>();
		if (AnnotationHelper.instance().isAnnotationAware()) {
			for (Annotation annotation : clz.getAnnotations()) {
				if (AnnotationHelper.instance().isAnnotationManaged(annotation)) {
					AnnotationMetadata annotationMetadata = new AnnotationMetadata(this, annotation.annotationType());
					annotationMetadata.setAttributesMap(AnnotationHelper.instance().getAttributesMap(annotation));
					this.annotations.add(annotationMetadata);
				}
			}
		}
	}
	
	public Set<FieldMetadata> getFields() {
		return fields;
	}
	
	public FieldMetadata getNamedField(final String fieldName) {
		if (StringUtils.isBlank(fieldName)) return null;
		for (FieldMetadata field : fields) {
			if (field.getName().equals(fieldName)) return field;
		}
		return null;
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
	
	public void addField(final FieldMetadata field) {
		if (field == null) throw new IllegalArgumentException("Unspecified field");
		fields.add(field);
	}
	
	public void setFields(final Set<FieldMetadata> fields) {
		if (fields == null) this.fields.clear(); else this.fields = fields;
	}
	
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder("ClassMetadata [");
		buffer.append('\n').append("qualifiedName=").append(qualifiedName)
			.append('\n').append("package=").append(packageName)
			.append('\n').append("name=").append(name)
			.append('\n').append("fields=[");
		for (FieldMetadata fieldMetadata : fields) {
			buffer.append('\n').append(fieldMetadata.toString());
		}
		buffer.append(']');
		buffer.append('\n').append("annotations=[");
		for (AnnotationMetadata annotationMetadata : annotations) {
			buffer.append('\n').append(annotationMetadata.toString());
		}
		buffer.append(']');
		return buffer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((qualifiedName == null) ? 0 : qualifiedName.hashCode());
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
		ClassMetadata other = (ClassMetadata) obj;
		if (qualifiedName == null) {
			if (other.qualifiedName != null)
				return false;
		} else if (!qualifiedName.equals(other.qualifiedName))
			return false;
		return true;
	}
}