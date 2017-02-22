package au.com.innodev.wmboost.data;

/**
 * Views a unit entry
 */
interface UnitEntryAccessor<T> extends HasKey {

	boolean isAssigned();
	
	T getVal() throws InexistentEntryException;

}
