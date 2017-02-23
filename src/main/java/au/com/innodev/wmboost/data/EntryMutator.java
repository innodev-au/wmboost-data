package au.com.innodev.wmboost.data;

/**
 * Changes an entry.
 */
interface EntryMutator<T> {

	void put(T value);	
		
	void remove() throws InexistentEntryException;
	
	void remove(RemoveEntryOption removeOption) throws InexistentEntryException;
}
