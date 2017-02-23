package au.com.innodev.wmboost.data;

/**
 * Views a unit entry
 */
interface UnitEntryAccessor<T> {

	T getVal() throws InexistentEntryException;

	T getNonNullVal() throws InexistentEntryException, UnexpectedEntryValueException;
}
