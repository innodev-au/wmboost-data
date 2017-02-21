package au.com.innodev.wmboost.data;

/**
 * Collection-related utilities
 */
final class CollectionUtil {

	private CollectionUtil() {
		
	}
	
	/**
	 * Returns whether all elements in the collection are of the given type 
	 * 
	 * @param collection collection
	 * @param type type to look for
	 * @return true if all elements are of type {@code type}; false, otherwise
	 */
	public static boolean areAllElementsOfType(Iterable<?> collection, Class<?> type) {
		for (Object object : collection) {
			if (! type.isInstance(object)) {
				return false;
			}
		}
		return true;
	}
}
