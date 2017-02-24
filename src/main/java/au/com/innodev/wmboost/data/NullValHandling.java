package au.com.innodev.wmboost.data;

/**
 * Specifies the behaviour when an entry contains a null value.
 * 
 */
public enum NullValHandling {
	/**
	 * An exception is thrown if the entry contains a null value
	 */
	FAIL,
	/**
	 * If the entry contains a null value, return the value specified as default
	 */
	RETURN_DEFAULT,
	/**
	 * If the entry contains a null value, return null.
	 */
	RETURN_NULL;
}
