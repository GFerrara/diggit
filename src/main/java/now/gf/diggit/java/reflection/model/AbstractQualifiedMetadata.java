package now.gf.diggit.java.reflection.model;

import java.io.Serializable;

/**
 * 
 * @author Giorgio Ferrara
 *
 */
abstract class AbstractQualifiedMetadata implements Serializable{
	private static final long serialVersionUID = 5683057811020874835L;

	protected Class<?> clz;
	protected String qualifiedName;
	protected String packageName;
	protected String name;
	
	public AbstractQualifiedMetadata(final Class<?> clz) {
		if (clz == null) throw new IllegalArgumentException("Unspecified class");

		this.clz = clz;
		this.qualifiedName = clz.getCanonicalName();
		this.packageName = clz.getPackage() == null ? null : clz.getPackage().getName() ;
		this.name = clz.getSimpleName();
	}
	
	public Class<?> getClz() {
		return clz;
	}
	
	public String getPackageName() {
		return packageName;
	}

	public String getName() {
		return name;
	}
	
	public String getQualifiedName() {
		return qualifiedName;
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
		AbstractQualifiedMetadata other = (AbstractQualifiedMetadata) obj;
		if (qualifiedName == null) {
			if (other.qualifiedName != null)
				return false;
		} else if (!qualifiedName.equals(other.qualifiedName))
			return false;
		return true;
	}
}