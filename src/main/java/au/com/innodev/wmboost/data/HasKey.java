package au.com.innodev.wmboost.data;

/**
 * An interface for entries, which have a key.
 */
public interface HasKey {
	
	/**
	 * Returns the key associated to the entry being referenced
	 * 
	 * @return key associated to the entry
	 */
	String getKey();
	
}
