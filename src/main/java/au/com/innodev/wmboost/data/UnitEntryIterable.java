/**
 * Copyright 2017 Innodev
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
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
