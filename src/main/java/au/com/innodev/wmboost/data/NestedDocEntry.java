package au.com.innodev.wmboost.data;

/**
 * A reference to an entry with a nested document as the value.
 * 
 * @see ItemEntry
 */
public interface NestedDocEntry extends ItemEntry<Document> {

	/**
	 * Creates a nested document. It creates an empty document and puts it in
	 * the entry.
	 * 
	 * @return the new nested document
	 */
	public Document putNew();
}
