package now.gf.diggit.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * <p>This class realizes a LRU cache by extending {@link LinkedHashMap}.
 * A maximum size must be provided in order to limit the number of entries 
 * stored into the cache.</p>
 * 
 * @author Giorgio Ferrara
 * @see LinkedHashMap
 */
public class LRUCache <K,V> extends LinkedHashMap<K, V>{
	private static final long serialVersionUID = 7391210019309654014L;
	private int maxSize;
	
	/**
	 * Builds a new <tt>LRUCache</tt> by specifying its maximum size.
	 * 
	 * @param maxSize maximum cache size: must be at least 1
	 */
	public LRUCache(final int maxSize) {
		super(16, 0.75f, true);
		if (maxSize < 1) throw new IllegalArgumentException("LRU cache must contain at least one entry");
		this.maxSize = maxSize;
	}
	
	@Override
	protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
		return size() > maxSize;
	}
	
	/**
	 * Returns maximum cache size.
	 * 
	 * @return maximum cache size
	 */
	public int getMaxSize() {
		return maxSize;
	}
}
