package au.com.innodev.wmboost.data;

/**
 * Changes an entry
 */
interface EntryMutator<T> extends HasKey {

	void put(T value);	
	
	void remove() throws InexistentEntryException;
	
	void remove(RemoveEntryOption removeOption) throws InexistentEntryException;
}
