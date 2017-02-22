package au.com.innodev.wmboost.data;

import java.util.Iterator;

/**
 * Allows iterating over unit entries in a document.
 */
class UnitEntryIterable implements Iterable<KeyValue> {

	private final Document document;
	
	public UnitEntryIterable(Document document) {
		super();
		this.document = document;
	}

	@Override
	public Iterator<KeyValue> iterator() {		
		return new UnitEntryIterator();
	}

	private class UnitEntryIterator implements Iterator<KeyValue> {
		private final Iterator<String> keys;
		public UnitEntryIterator() {
			this.keys = document.getKeys().iterator();
		}

		@Override
		public boolean hasNext() {
			return keys.hasNext();
		}

		@Override
		public KeyValue next() {
			String key = keys.next();
			Object value = document.entry(key).getVal();
			KeyValue keyVal = new ImmutableKeyValue(key, value);
			return keyVal;
		}
	}
}
